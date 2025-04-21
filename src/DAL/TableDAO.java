package DAL;

import DTO.TableDTO;
import java.sql.*;
import java.util.*;

public class TableDAO {
    public List<TableDTO> getAllTables() {
        List<TableDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tables";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TableDTO t = new TableDTO(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("status")
                );
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int tableId, String status) {
        String sql = "UPDATE tables SET status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, tableId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}