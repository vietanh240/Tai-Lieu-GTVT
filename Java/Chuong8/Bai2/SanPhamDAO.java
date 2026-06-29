package Bai2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham ORDER BY tenSP";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapSP(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<SanPham> search(String keyword) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE tenSP LIKE ? ORDER BY tenSP";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapSP(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addSanPham(SanPham sp) {
        String sql = "INSERT INTO sanpham (tenSP, donGia, soLuong) VALUES (?,?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateSoLuong(int idSP, int soLuongGiam) {
        String sql = "UPDATE sanpham SET soLuong = soLuong - ? WHERE id = ? AND soLuong >= ?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongGiam);
            ps.setInt(2, idSP);
            ps.setInt(3, soLuongGiam);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private SanPham mapSP(ResultSet rs) throws SQLException {
        return new SanPham(rs.getInt("id"), rs.getString("tenSP"),
                rs.getDouble("donGia"), rs.getInt("soLuong"));
    }

    // ==================== HÓA ĐƠN ====================

    /**
     * Lưu hóa đơn + chi tiết vào DB, tự trừ tồn kho.
     * Trả về id hóa đơn vừa tạo, hoặc -1 nếu lỗi.
     */
    public int luuHoaDon(List<GioHangItem> cart, double tongTien) {
        String sqlHD   = "INSERT INTO hoadon (ngayLap, tongTien) VALUES (NOW(), ?)";
        String sqlCTHD = "INSERT INTO chitiethoadon (idHD, idSP, soLuong, donGia) VALUES (?,?,?,?)";
        String sqlTru  = "UPDATE sanpham SET soLuong = soLuong - ? WHERE id = ? AND soLuong >= ?";

        Connection con = dbconnect.getConnect();
        if (con == null) return -1;

        try {
            con.setAutoCommit(false); // Transaction

            // 1. Insert hóa đơn
            int idHD = -1;
            try (PreparedStatement ps = con.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, tongTien);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idHD = rs.getInt(1);
            }
            if (idHD < 0) { con.rollback(); return -1; }

            // 2. Insert chi tiết + trừ tồn
            for (GioHangItem item : cart) {
                try (PreparedStatement psCT = con.prepareStatement(sqlCTHD);
                     PreparedStatement psTru = con.prepareStatement(sqlTru)) {

                    psCT.setInt(1, idHD);
                    psCT.setInt(2, item.getSanPham().getId());
                    psCT.setInt(3, item.getSoLuong());
                    psCT.setDouble(4, item.getSanPham().getDonGia());
                    psCT.executeUpdate();

                    psTru.setInt(1, item.getSoLuong());
                    psTru.setInt(2, item.getSanPham().getId());
                    psTru.setInt(3, item.getSoLuong());
                    int affected = psTru.executeUpdate();
                    if (affected == 0) {
                        con.rollback();
                        return -2; // Không đủ hàng
                    }
                }
            }

            con.commit();
            return idHD;

        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            return -1;
        } finally {
            try { con.setAutoCommit(true); con.close(); } catch (Exception ignored) {}
        }
    }

    // Lấy danh sách hóa đơn
    public List<String[]> getDanhSachHoaDon() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT h.id, h.ngayLap, h.tongTien, COUNT(c.idSP) as soMat " +
                     "FROM hoadon h LEFT JOIN chitiethoadon c ON h.id=c.idHD " +
                     "GROUP BY h.id ORDER BY h.ngayLap DESC LIMIT 50";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("ngayLap"),
                    String.valueOf(rs.getDouble("tongTien")),
                    String.valueOf(rs.getInt("soMat"))
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
