package DAL;

import DTO.CustomerDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements GenericDAO<CustomerDTO> {

    @Override
    public List<CustomerDTO> getAll() throws DatabaseException {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY customerID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách khách hàng: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public CustomerDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM customer WHERE customerID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm khách hàng: " + e.getMessage(), e);
        }
        return null;
    }

    public CustomerDTO getByPhone(String phone) throws DatabaseException {
        String sql = "SELECT * FROM customer WHERE phone = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm SĐT khách hàng: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean add(CustomerDTO customer) throws DatabaseException {
        String sql = "INSERT INTO customer (firstName, lastName, phone, email, address, totalVisits) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getAddress());
            ps.setInt(6, customer.getTotalVisits());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) customer.setCustomerId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm khách hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(CustomerDTO customer) throws DatabaseException {
        String sql = "UPDATE customer SET firstName=?, lastName=?, phone=?, email=?, address=?, totalVisits=? WHERE customerID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getAddress());
            ps.setInt(6, customer.getTotalVisits());
            ps.setInt(7, customer.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật khách hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM customer WHERE customerID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa khách hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerDTO> search(String keyword) throws DatabaseException {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE firstName LIKE ? OR lastName LIKE ? OR phone LIKE ?";
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
            throw new DatabaseException("Lỗi khi tìm kiếm khách hàng: " + e.getMessage(), e);
        }
        return list;
    }

    private CustomerDTO mapRow(ResultSet rs) throws SQLException {
        CustomerDTO dto = new CustomerDTO(
            rs.getInt("customerID"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getInt("totalVisits")
        );
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) {
            dto.setCreatedAt(ts.toLocalDateTime());
        }
        return dto;
    }
}
