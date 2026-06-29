package Bai1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {

    public List<SinhVien> getAll(String orderBy) {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT id, hoTen, lop, diemTB, email FROM sinhvien ORDER BY " + orderBy;
        try (Connection con = dbconnect.getConnect();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SinhVien> search(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT id, hoTen, lop, diemTB, email FROM sinhvien " +
                     "WHERE hoTen LIKE ? OR CAST(id AS CHAR) LIKE ? ORDER BY id";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(SinhVien sv) {
        String sql = "INSERT INTO sinhvien(hoTen, lop, diemTB, email) VALUES(?,?,?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setString(2, sv.getLop());
            ps.setDouble(3, sv.getDiemTB());
            ps.setString(4, sv.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SinhVien sv) {
        String sql = "UPDATE sinhvien SET hoTen=?, lop=?, diemTB=?, email=? WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setString(2, sv.getLop());
            ps.setDouble(3, sv.getDiemTB());
            ps.setString(4, sv.getEmail());
            ps.setInt   (5, sv.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM sinhvien WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> thongKeTheoLop() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT lop, COUNT(*) AS soSV, ROUND(AVG(diemTB),2) AS diemTBLop " +
                     "FROM sinhvien GROUP BY lop ORDER BY lop";
        try (Connection con = dbconnect.getConnect();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("lop"),
                    rs.getInt("soSV"),
                    rs.getDouble("diemTBLop")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private SinhVien mapRow(ResultSet rs) throws SQLException {
        return new SinhVien(
            rs.getInt("id"),
            rs.getString("hoTen"),
            rs.getString("lop"),
            rs.getDouble("diemTB"),
            rs.getString("email")
        );
    }
}
