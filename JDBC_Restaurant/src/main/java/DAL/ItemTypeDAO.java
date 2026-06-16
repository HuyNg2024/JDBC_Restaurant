package DAL;

import DTO.ItemTypeDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemTypeDAO implements GenericDAO<ItemTypeDTO> {

    @Override
    public List<ItemTypeDTO> getAll() throws DatabaseException {
        List<ItemTypeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item_type ORDER BY typeID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh mục món: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public ItemTypeDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM item_type WHERE typeID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm danh mục món: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean add(ItemTypeDTO itemType) throws DatabaseException {
        String sql = "INSERT INTO item_type (typeName, description) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, itemType.getTypeName());
            ps.setString(2, itemType.getDescription());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) itemType.setTypeId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm danh mục món: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(ItemTypeDTO itemType) throws DatabaseException {
        String sql = "UPDATE item_type SET typeName=?, description=? WHERE typeID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemType.getTypeName());
            ps.setString(2, itemType.getDescription());
            ps.setInt(3, itemType.getTypeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật danh mục món: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM item_type WHERE typeID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa danh mục món (Có thể do danh mục này đang chứa món ăn): " + e.getMessage(), e);
        }
    }

    @Override
    public List<ItemTypeDTO> search(String keyword) throws DatabaseException {
        List<ItemTypeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item_type WHERE typeName LIKE ? OR description LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm danh mục: " + e.getMessage(), e);
        }
        return list;
    }

    private ItemTypeDTO mapRow(ResultSet rs) throws SQLException {
        return new ItemTypeDTO(
            rs.getInt("typeID"),
            rs.getString("typeName"),
            rs.getString("description")
        );
    }
}
