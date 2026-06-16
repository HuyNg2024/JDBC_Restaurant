package DAL;

import DTO.TransactionDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements GenericDAO<TransactionDTO> {

    @Override
    public List<TransactionDTO> getAll() throws DatabaseException {
        List<TransactionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction ORDER BY createdAt DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách thanh toán: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public TransactionDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM transaction WHERE transactionID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm thanh toán: " + e.getMessage(), e);
        }
        return null;
    }

    public TransactionDTO getByOrder(int orderId) throws DatabaseException {
        String sql = "SELECT * FROM transaction WHERE orderID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm thanh toán của đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean add(TransactionDTO trans) throws DatabaseException {
        String sql = "INSERT INTO transaction (orderID, customerID, amount, paymentMethod, status, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (trans.getOrderId() > 0) ps.setInt(1, trans.getOrderId());
            else ps.setNull(1, Types.INTEGER);
            
            if (trans.getCustomerId() > 0) ps.setInt(2, trans.getCustomerId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setDouble(3, trans.getAmount());
            ps.setString(4, trans.getPaymentMethod());
            ps.setString(5, trans.getStatus());
            ps.setString(6, trans.getNote());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) trans.setTransactionId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tạo thanh toán: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(TransactionDTO trans) throws DatabaseException {
        String sql = "UPDATE transaction SET orderID=?, customerID=?, amount=?, paymentMethod=?, status=?, note=? WHERE transactionID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (trans.getOrderId() > 0) ps.setInt(1, trans.getOrderId());
            else ps.setNull(1, Types.INTEGER);
            
            if (trans.getCustomerId() > 0) ps.setInt(2, trans.getCustomerId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setDouble(3, trans.getAmount());
            ps.setString(4, trans.getPaymentMethod());
            ps.setString(5, trans.getStatus());
            ps.setString(6, trans.getNote());
            ps.setInt(7, trans.getTransactionId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật thanh toán: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM transaction WHERE transactionID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa thanh toán: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TransactionDTO> search(String keyword) throws DatabaseException {
        List<TransactionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE transactionID LIKE ? OR note LIKE ? OR paymentMethod LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm thanh toán: " + e.getMessage(), e);
        }
        return list;
    }

    private TransactionDTO mapRow(ResultSet rs) throws SQLException {
        TransactionDTO dto = new TransactionDTO(
            rs.getInt("transactionID"),
            rs.getInt("orderID"),
            rs.getInt("customerID"),
            rs.getDouble("amount"),
            rs.getString("paymentMethod"),
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
