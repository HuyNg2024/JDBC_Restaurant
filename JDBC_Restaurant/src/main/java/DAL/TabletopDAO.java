package DAL;

import DTO.TabletopDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TabletopDAO implements GenericDAO<TabletopDTO> {

    @Override
    public List<TabletopDTO> getAll() throws DatabaseException {
        List<TabletopDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tabletop ORDER BY tableCode ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách bàn ăn: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public TabletopDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM tabletop WHERE tableID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm bàn ăn: " + e.getMessage(), e);
        }
        return null;
    }

    public List<TabletopDTO> getByStatus(String status) throws DatabaseException {
        List<TabletopDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tabletop WHERE status = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc bàn ăn theo trạng thái: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean add(TabletopDTO table) throws DatabaseException {
        String sql = "INSERT INTO tabletop (tableCode, capacity, status, floor) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, table.getTableCode());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus());
            ps.setInt(4, table.getFloor());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) table.setTableId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm bàn ăn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(TabletopDTO table) throws DatabaseException {
        String sql = "UPDATE tabletop SET tableCode=?, capacity=?, status=?, floor=? WHERE tableID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table.getTableCode());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus());
            ps.setInt(4, table.getFloor());
            ps.setInt(5, table.getTableId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật bàn ăn: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(int tableId, String status) throws DatabaseException {
        String sql = "UPDATE tabletop SET status=? WHERE tableID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, tableId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật trạng thái bàn ăn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM tabletop WHERE tableID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa bàn ăn: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TabletopDTO> search(String keyword) throws DatabaseException {
        List<TabletopDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tabletop WHERE tableCode LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm bàn ăn: " + e.getMessage(), e);
        }
        return list;
    }

    private TabletopDTO mapRow(ResultSet rs) throws SQLException {
        return new TabletopDTO(
            rs.getInt("tableID"),
            rs.getString("tableCode"),
            rs.getInt("capacity"),
            rs.getString("status"),
            rs.getInt("floor")
        );
    }
}
