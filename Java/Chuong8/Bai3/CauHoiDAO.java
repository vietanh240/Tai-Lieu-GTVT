package Bai3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CauHoiDAO {

    public List<CauHoi> getAll() {
        List<CauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM cauhoi ORDER BY id";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addCauHoi(CauHoi cq) {
        String sql = "INSERT INTO cauhoi (noiDung, A, B, C, D, dapAn) VALUES (?,?,?,?,?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cq.getNoiDung());
            ps.setString(2, cq.getA());
            ps.setString(3, cq.getB());
            ps.setString(4, cq.getC());
            ps.setString(5, cq.getD());
            ps.setString(6, cq.getDapAn());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /** Ghi lịch sử chơi */
    public void ghiLichSu(String tenNguoiChoi, int diem, int tongCau) {
        String sql = "INSERT INTO lichsu (tenNguoiChoi, diem, tongCau, thoiGian) VALUES (?,?,?,NOW())";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenNguoiChoi);
            ps.setInt(2, diem);
            ps.setInt(3, tongCau);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Lấy lịch sử chơi (50 gần nhất) */
    public List<String[]> getLichSu() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT tenNguoiChoi, diem, tongCau, thoiGian FROM lichsu ORDER BY thoiGian DESC LIMIT 50";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("tenNguoiChoi"),
                    rs.getString("diem") + "/" + rs.getString("tongCau"),
                    rs.getString("thoiGian")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private CauHoi map(ResultSet rs) throws SQLException {
        return new CauHoi(
            rs.getInt("id"),
            rs.getString("noiDung"),
            rs.getString("A"), rs.getString("B"),
            rs.getString("C"), rs.getString("D"),
            rs.getString("dapAn")
        );
    }
}
