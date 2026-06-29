package Bai5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThuVienDAO {

    public List<Sach> getAllSach() {
        List<Sach> list = new ArrayList<>();
        String sql = "SELECT * FROM sach ORDER BY tenSach";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapSach(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Sach> searchSach(String kw) {
        List<Sach> list = new ArrayList<>();
        String sql = "SELECT * FROM sach WHERE tenSach LIKE ? OR tacGia LIKE ? ORDER BY tenSach";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%"); ps.setString(2, "%" + kw + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapSach(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addSach(Sach s) {
        String sql = "INSERT INTO sach (tenSach,tacGia,namXB,tinhTrang) VALUES (?,?,?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTenSach()); ps.setString(2, s.getTacGia());
            ps.setString(3, s.getNamXB());   ps.setString(4, s.getTinhTrang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateSach(Sach s) {
        String sql = "UPDATE sach SET tenSach=?,tacGia=?,namXB=?,tinhTrang=? WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTenSach()); ps.setString(2, s.getTacGia());
            ps.setString(3, s.getNamXB());   ps.setString(4, s.getTinhTrang());
            ps.setInt(5, s.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteSach(int id) {
        String sql = "DELETE FROM sach WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<DocGia> getAllDocGia() {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM docgia ORDER BY hoTen";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapDocGia(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addDocGia(DocGia dg) {
        String sql = "INSERT INTO docgia (hoTen,ngaySinh) VALUES (?,?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dg.getHoTen()); ps.setString(2, dg.getNgaySinh());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateDocGia(DocGia dg) {
        String sql = "UPDATE docgia SET hoTen=?,ngaySinh=? WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dg.getHoTen()); ps.setString(2, dg.getNgaySinh());
            ps.setInt(3, dg.getId()); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteDocGia(int id) {
        String sql = "DELETE FROM docgia WHERE id=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<MuonSach> getAllMuon() {
        List<MuonSach> list = new ArrayList<>();
        String sql = "SELECT m.idMuon, m.idDocGia, m.idSach, d.hoTen, s.tenSach, " +
                     "DATE_FORMAT(m.ngayMuon,'%d/%m/%Y') as ngayMuon, " +
                     "IFNULL(DATE_FORMAT(m.ngayTra,'%d/%m/%Y'),'Chưa trả') as ngayTra " +
                     "FROM muonsach m " +
                     "JOIN docgia d ON m.idDocGia=d.id " +
                     "JOIN sach   s ON m.idSach=s.id " +
                     "ORDER BY m.ngayMuon DESC";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new MuonSach(
                rs.getInt("idMuon"), rs.getInt("idDocGia"), rs.getInt("idSach"),
                rs.getString("hoTen"), rs.getString("tenSach"),
                rs.getString("ngayMuon"), rs.getString("ngayTra")
            ));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean muonSach(int idDocGia, int idSach) {
        String sql = "INSERT INTO muonsach (idDocGia,idSach,ngayMuon) VALUES (?,?,CURDATE())";
        String upd = "UPDATE sach SET tinhTrang='Đang mượn' WHERE id=?";
        Connection con = dbconnect.getConnect();
        if (con == null) return false;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idDocGia); ps.setInt(2, idSach); ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(upd)) {
                ps.setInt(1, idSach); ps.executeUpdate();
            }
            con.commit(); return true;
        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ignored) {}
            e.printStackTrace(); return false;
        } finally {
            try { con.setAutoCommit(true); con.close(); } catch (Exception ignored) {}
        }
    }

    public boolean traSach(int idMuon, int idSach) {
        String sql = "UPDATE muonsach SET ngayTra=CURDATE() WHERE idMuon=?";
        String upd = "UPDATE sach SET tinhTrang='Còn' WHERE id=?";
        Connection con = dbconnect.getConnect();
        if (con == null) return false;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMuon); ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(upd)) {
                ps.setInt(1, idSach); ps.executeUpdate();
            }
            con.commit(); return true;
        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ignored) {}
            e.printStackTrace(); return false;
        } finally {
            try { con.setAutoCommit(true); con.close(); } catch (Exception ignored) {}
        }
    }

    public List<String[]> sachTreHan() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT d.hoTen, s.tenSach, DATE_FORMAT(m.ngayMuon,'%d/%m/%Y') as ngayMuon, " +
                     "DATEDIFF(CURDATE(), m.ngayMuon) as soNgay " +
                     "FROM muonsach m JOIN docgia d ON m.idDocGia=d.id JOIN sach s ON m.idSach=s.id " +
                     "WHERE m.ngayTra IS NULL AND DATEDIFF(CURDATE(), m.ngayMuon) > 14 " +
                     "ORDER BY soNgay DESC";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new String[]{
                rs.getString("hoTen"), rs.getString("tenSach"),
                rs.getString("ngayMuon"), rs.getString("soNgay") + " ngày"
            });
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<String[]> thongKeSachMuonNhieu() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT s.tenSach, s.tacGia, COUNT(m.idMuon) as soLanMuon " +
                     "FROM sach s LEFT JOIN muonsach m ON s.id=m.idSach " +
                     "GROUP BY s.id ORDER BY soLanMuon DESC LIMIT 20";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new String[]{
                rs.getString("tenSach"), rs.getString("tacGia"),
                String.valueOf(rs.getInt("soLanMuon"))
            });
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Sach mapSach(ResultSet rs) throws SQLException {
        return new Sach(rs.getInt("id"), rs.getString("tenSach"),
                rs.getString("tacGia"), rs.getString("namXB"), rs.getString("tinhTrang"));
    }

    private DocGia mapDocGia(ResultSet rs) throws SQLException {
        return new DocGia(rs.getInt("id"), rs.getString("hoTen"), rs.getString("ngaySinh"));
    }
}
