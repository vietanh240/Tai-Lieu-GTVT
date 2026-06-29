package Bai6;

import java.security.MessageDigest;
import java.sql.*;

public class NguoiDungDAO {

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); return input; }
    }

    /**
     * 
     * @return 
     */
    public String login(String username, String password) {
        String sql = "SELECT hoTen FROM nguoidung WHERE username=? AND password=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, md5(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("hoTen");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean doiMatKhau(String username, String matKhauCu, String matKhauMoi) {
        if (login(username, matKhauCu) == null) return false;
        String sql = "UPDATE nguoidung SET password=? WHERE username=?";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, md5(matKhauMoi));
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public void ghiLog(String username, boolean thanhCong) {
        String sql = "INSERT INTO loginlog (username, thoiGian, ketQua) VALUES (?,NOW(),?)";
        try (Connection con = dbconnect.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, thanhCong ? "Thành công" : "Thất bại");
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public java.util.List<String[]> getLog() {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT username, thoiGian, ketQua FROM loginlog ORDER BY thoiGian DESC LIMIT 50";
        try (Connection con = dbconnect.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new String[]{
                rs.getString("username"), rs.getString("thoiGian"), rs.getString("ketQua")
            });
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
