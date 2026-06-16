package DAL;

import DTO.OrderItemDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Note: Does not implement GenericDAO because it uses composite primary key (orderID, itemID).
 */
public class OrderItemDAO {

    public List<OrderItemDTO> getByOrderId(int orderId) throws DatabaseException {
        List<OrderItemDTO> list = new ArrayList<>();
        String sql = "SELECT oi.*, i.itemName FROM order_item oi " +
                     "JOIN item i ON oi.itemID = i.itemID " +
                     "WHERE oi.orderID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderItemDTO(
                        rs.getInt("orderID"),
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean addItem(OrderItemDTO item) throws DatabaseException {
        String sql = "INSERT INTO order_item (orderID, itemID, quantity, unitPrice) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + ?, unitPrice = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            // For update on duplicate
            ps.setInt(5, item.getQuantity());
            ps.setDouble(6, item.getUnitPrice());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm món vào đơn hàng: " + e.getMessage(), e);
        }
    }

    public boolean updateItemQuantity(int orderId, int itemId, int quantity) throws DatabaseException {
        String sql = "UPDATE order_item SET quantity = ? WHERE orderID = ? AND itemID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật số lượng món: " + e.getMessage(), e);
        }
    }

    public boolean removeItem(int orderId, int itemId) throws DatabaseException {
        String sql = "DELETE FROM order_item WHERE orderID = ? AND itemID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa món khỏi đơn hàng: " + e.getMessage(), e);
        }
    }

    public boolean deleteByOrderId(int orderId) throws DatabaseException {
        String sql = "DELETE FROM order_item WHERE orderID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }
}
