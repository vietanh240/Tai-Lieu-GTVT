package Bai5;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbconnect {
    private static final String URL  = "jdbc:mysql://localhost:3306/thuvien";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) { e.printStackTrace(); return null; }
    }
}
