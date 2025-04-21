package DAL;

import DTO.ItemDTO;

import java.sql.*;
import java.util.ArrayList;

public class ItemDAL {
    public ArrayList<ItemDTO> getAllItems() {
        ArrayList<ItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Item";

        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ItemDTO item = new ItemDTO(
                        rs.getInt("ItemID"),
                        rs.getString("ItemName"),
                        rs.getInt("staffID"),
                        rs.getString("ItemDetail"),
                        rs.getInt("productMin"),
                        rs.getDouble("ItemPrice"),
                        rs.getInt("Calories"),
                        rs.getInt("ItemType")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            System.out.println("❌ ItemDAL - getAllItems: " + e.getMessage());
        }
        return list;
    }

    public boolean insertItem(ItemDTO item) {
        String sql = "INSERT INTO Item (ItemName, staffID, ItemDetail, productMin, ItemPrice, Calories, ItemType) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setInt(2, item.getStaffID());
            stmt.setString(3, item.getItemDetail());
            stmt.setInt(4, item.getProductMin());
            stmt.setDouble(5, item.getItemPrice());
            stmt.setInt(6, item.getCalories());
            stmt.setInt(7, item.getItemType());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ insertItem: " + e.getMessage());
            return false;
        }
    }

    public boolean updateItem(ItemDTO item) {
        String sql = "UPDATE Item SET ItemName=?, staffID=?, ItemDetail=?, productMin=?, ItemPrice=?, Calories=?, ItemType=? WHERE ItemID=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setInt(2, item.getStaffID());
            stmt.setString(3, item.getItemDetail());
            stmt.setInt(4, item.getProductMin());
            stmt.setDouble(5, item.getItemPrice());
            stmt.setInt(6, item.getCalories());
            stmt.setInt(7, item.getItemType());
            stmt.setInt(8, item.getItemID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ updateItem: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItem(int itemID) {
        String sql = "DELETE FROM Item WHERE ItemID=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ deleteItem: " + e.getMessage());
            return false;
        }
    }
}
