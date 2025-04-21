package DAL;

import DTO.TabletopDTO;

import java.sql.*;
import java.util.ArrayList;

public class TabletopDAL {
    public ArrayList<TabletopDTO> getAllTables() {
        ArrayList<TabletopDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM TableTop";

        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TabletopDTO table = new TabletopDTO(
                        rs.getInt("tableID"),
                        (Integer) rs.getObject("Customer_ID"),  // hỗ trợ null
                        rs.getString("Table_Code"),
                        rs.getString("Status"),
                        (Integer) rs.getObject("Capacity"),
                        (Integer) rs.getObject("Current")
                );
                list.add(table);
            }

        } catch (SQLException e) {
            System.out.println("❌ getAllTables: " + e.getMessage());
        }
        return list;
    }

    public boolean insertTable(TabletopDTO table) {
        String sql = "INSERT INTO TableTop (Customer_ID, Table_Code, Status, Capacity, Current) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (table.getCustomerID() != null) {
                stmt.setInt(1, table.getCustomerID());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setString(2, table.getTableCode());
            stmt.setString(3, table.getStatus());
            stmt.setInt(4, table.getCapacity());
            stmt.setInt(5, table.getCurrent());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ insertTable: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTable(TabletopDTO table) {
        String sql = "UPDATE TableTop SET Customer_ID=?, Table_Code=?, Status=?, Capacity=?, Current=? WHERE tableID=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (table.getCustomerID() != null) {
                stmt.setInt(1, table.getCustomerID());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setString(2, table.getTableCode());
            stmt.setString(3, table.getStatus());
            stmt.setInt(4, table.getCapacity());
            stmt.setInt(5, table.getCurrent());
            stmt.setInt(6, table.getTableID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ updateTable: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTable(int tableID) {
        String sql = "DELETE FROM TableTop WHERE tableID=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ deleteTable: " + e.getMessage());
            return false;
        }
    }
}
