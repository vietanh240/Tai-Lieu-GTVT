package Bai4;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public String login(String username, String password) {
        String sql = "SELECT role FROM users WHERE username=? AND password=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> getAll(String orderBy) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT maNV,tenNV,phongBan,heSoLuong,luongCoBan " +
                     "FROM nhanvien ORDER BY " + orderBy;
        try (Connection con = dbconnect.getConnect();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Tìm kiếm theo phòng ban hoặc tên ────────────────────
    public List<NhanVien> search(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT maNV,tenNV,phongBan,heSoLuong,luongCoBan " +
                     "FROM nhanvien WHERE phongBan LIKE ? OR tenNV LIKE ? ORDER BY phongBan";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Thêm ────────────────────────────────────────────────
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO nhanvien(maNV,tenNV,phongBan,heSoLuong,luongCoBan) VALUES(?,?,?,?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getPhongBan());
            ps.setDouble(4, nv.getHeSoLuong());
            ps.setDouble(5, nv.getLuongCoBan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Sửa ─────────────────────────────────────────────────
    public boolean update(NhanVien nv) {
        String sql = "UPDATE nhanvien SET tenNV=?,phongBan=?,heSoLuong=?,luongCoBan=? WHERE maNV=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getPhongBan());
            ps.setDouble(3, nv.getHeSoLuong());
            ps.setDouble(4, nv.getLuongCoBan());
            ps.setString(5, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Xóa ─────────────────────────────────────────────────
    public boolean delete(String maNV) {
        String sql = "DELETE FROM nhanvien WHERE maNV=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Báo cáo theo phòng ban ───────────────────────────────
    public List<Object[]> baoCaoTheoPhongBan() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT phongBan, COUNT(*) AS soNV, " +
                     "ROUND(AVG(heSoLuong),2) AS hsTB, " +
                     "ROUND(SUM(heSoLuong * luongCoBan),0) AS tongQuy " +
                     "FROM nhanvien GROUP BY phongBan ORDER BY phongBan";
        try (Connection con = dbconnect.getConnect();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("phongBan"),
                    rs.getInt("soNV"),
                    rs.getDouble("hsTB"),
                    rs.getDouble("tongQuy")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private NhanVien mapRow(ResultSet rs) throws SQLException {
        return new NhanVien(
            rs.getString("maNV"),
            rs.getString("tenNV"),
            rs.getString("phongBan"),
            rs.getDouble("heSoLuong"),
            rs.getDouble("luongCoBan")
        );
    }
}
