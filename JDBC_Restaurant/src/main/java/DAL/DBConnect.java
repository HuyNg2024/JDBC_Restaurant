package DAL;

import config.AppConfig;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager.
 * Uses AppConfig to get external credentials.
 */
public class DBConnect {

    /**
     * Get a connection to the database.
     * @return Connection object
     * @throws DatabaseException if connection fails
     */
    public static Connection getConnection() throws DatabaseException {
        AppConfig config = AppConfig.getInstance();
        try {
            Class.forName(config.getDbDriver());
            return DriverManager.getConnection(
                config.getDbUrl(), 
                config.getDbUser(), 
                config.getDbPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Không tìm thấy MySQL Driver: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new DatabaseException("Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra config.properties.\nLỗi: " + e.getMessage(), e);
        }
    }

    /**
     * Utility to safely close database resources.
     */
    public static void closeQuietly(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ignored) {
                    // Ignore close exceptions
                }
            }
        }
    }

    /**
     * Test the database connection.
     * @return true if successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (DatabaseException | SQLException e) {
            return false;
        }
    }
}
