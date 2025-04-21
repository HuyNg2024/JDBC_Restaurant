package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurantmanage";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            
            System.out.println(" DBConnect - Lỗi SQL: " + e.getMessage());
            
            return null;
        } catch (ClassNotFoundException e) {
            
            System.out.println(" DBConnect - Lỗi bất thường: " + e.getMessage());
            return null;
        }
    }
    
//    public static void main(String[] args) {
//        Connection conn = DBConnect.getConnection();
//
//        if (conn != null) {
//            System.out.println(" Kết nối cơ sở dữ liệu thành công!");
//        } else {
//            System.out.println(" Kết nối cơ sở dữ liệu thất bại!");
//        }
//    }
}
