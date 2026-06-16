package DAL;

import DTO.OrderDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDAO<OrderDTO> {

    @Override
    public List<OrderDTO> getAll() throws DatabaseException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM `order` ORDER BY createdAt DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public OrderDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM `order` WHERE orderID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }

    public List<OrderDTO> getByTable(int tableId) throws DatabaseException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE tableID = ? ORDER BY createdAt DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tableId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc đơn hàng theo bàn: " + e.getMessage(), e);
        }
        return list;
    }

    public List<OrderDTO> getByStatus(String status) throws DatabaseException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE status = ? ORDER BY createdAt DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc đơn hàng theo trạng thái: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean add(OrderDTO order) throws DatabaseException {
        String sql = "INSERT INTO `order` (tableID, customerID, totalPrice, status, note) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (order.getTableId() > 0) ps.setInt(1, order.getTableId());
            else ps.setNull(1, Types.INTEGER);
            
            if (order.getCustomerId() > 0) ps.setInt(2, order.getCustomerId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setDouble(3, order.getTotalPrice());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getNote());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) order.setOrderId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tạo đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(OrderDTO order) throws DatabaseException {
        String sql = "UPDATE `order` SET tableID=?, customerID=?, totalPrice=?, status=?, note=? WHERE orderID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (order.getTableId() > 0) ps.setInt(1, order.getTableId());
            else ps.setNull(1, Types.INTEGER);
            
            if (order.getCustomerId() > 0) ps.setInt(2, order.getCustomerId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setDouble(3, order.getTotalPrice());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getNote());
            ps.setInt(6, order.getOrderId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật đơn hàng: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(int orderId, String status) throws DatabaseException {
        String sql = "UPDATE `order` SET status=? WHERE orderID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM `order` WHERE orderID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrderDTO> search(String keyword) throws DatabaseException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.* FROM `order` o LEFT JOIN customer c ON o.customerID = c.customerID " +
                     "WHERE o.note LIKE ? OR o.orderID LIKE ? OR c.phone LIKE ? OR c.firstName LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm đơn hàng: " + e.getMessage(), e);
        }
        return list;
    }

    private OrderDTO mapRow(ResultSet rs) throws SQLException {
        OrderDTO dto = new OrderDTO(
            rs.getInt("orderID"),
            rs.getInt("tableID"),
            rs.getInt("customerID"),
            rs.getDouble("totalPrice"),
            rs.getString("status"),
            rs.getString("note")
        );
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) {
            dto.setCreatedAt(ts.toLocalDateTime());
        }
        return dto;
    }
}
