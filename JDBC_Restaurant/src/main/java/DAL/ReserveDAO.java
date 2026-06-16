package DAL;

import DTO.ReserveDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReserveDAO implements GenericDAO<ReserveDTO> {

    @Override
    public List<ReserveDTO> getAll() throws DatabaseException {
        List<ReserveDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM reserve ORDER BY reserveTime DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách đặt bàn: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public ReserveDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM reserve WHERE reserveID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm đặt bàn: " + e.getMessage(), e);
        }
        return null;
    }

    public List<ReserveDTO> getByCustomer(int customerId) throws DatabaseException {
        List<ReserveDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM reserve WHERE customerID = ? ORDER BY reserveTime DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm đặt bàn theo khách hàng: " + e.getMessage(), e);
        }
        return list;
    }

    public List<ReserveDTO> getByDate(LocalDate date) throws DatabaseException {
        List<ReserveDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM reserve WHERE DATE(reserveTime) = ? ORDER BY reserveTime ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc đặt bàn theo ngày: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean add(ReserveDTO reserve) throws DatabaseException {
        String sql = "INSERT INTO reserve (customerID, tableID, reserveTime, guestCount, note, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (reserve.getCustomerId() > 0) ps.setInt(1, reserve.getCustomerId());
            else ps.setNull(1, Types.INTEGER);
            
            if (reserve.getTableId() > 0) ps.setInt(2, reserve.getTableId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setTimestamp(3, Timestamp.valueOf(reserve.getReserveTime()));
            ps.setInt(4, reserve.getGuestCount());
            ps.setString(5, reserve.getNote());
            ps.setString(6, reserve.getStatus());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) reserve.setReserveId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tạo đặt bàn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(ReserveDTO reserve) throws DatabaseException {
        String sql = "UPDATE reserve SET customerID=?, tableID=?, reserveTime=?, guestCount=?, note=?, status=? WHERE reserveID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (reserve.getCustomerId() > 0) ps.setInt(1, reserve.getCustomerId());
            else ps.setNull(1, Types.INTEGER);
            
            if (reserve.getTableId() > 0) ps.setInt(2, reserve.getTableId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setTimestamp(3, Timestamp.valueOf(reserve.getReserveTime()));
            ps.setInt(4, reserve.getGuestCount());
            ps.setString(5, reserve.getNote());
            ps.setString(6, reserve.getStatus());
            ps.setInt(7, reserve.getReserveId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật đặt bàn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM reserve WHERE reserveID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa đặt bàn: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ReserveDTO> search(String keyword) throws DatabaseException {
        List<ReserveDTO> list = new ArrayList<>();
        // Search by customer name via join, or by note
        String sql = "SELECT r.* FROM reserve r LEFT JOIN customer c ON r.customerID = c.customerID " +
                     "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR c.phone LIKE ? OR r.note LIKE ? " +
                     "ORDER BY r.reserveTime DESC";
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
            throw new DatabaseException("Lỗi khi tìm kiếm đặt bàn: " + e.getMessage(), e);
        }
        return list;
    }

    private ReserveDTO mapRow(ResultSet rs) throws SQLException {
        ReserveDTO dto = new ReserveDTO(
            rs.getInt("reserveID"),
            rs.getInt("customerID"),
            rs.getInt("tableID"),
            rs.getTimestamp("reserveTime").toLocalDateTime(),
            rs.getInt("guestCount"),
            rs.getString("note"),
            rs.getString("status")
        );
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) {
            dto.setCreatedAt(ts.toLocalDateTime());
        }
        return dto;
    }
}
