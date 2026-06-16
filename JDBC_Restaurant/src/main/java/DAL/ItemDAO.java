package DAL;

import DTO.ItemDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements GenericDAO<ItemDTO> {

    @Override
    public List<ItemDTO> getAll() throws DatabaseException {
        List<ItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item ORDER BY itemID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách món ăn: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public ItemDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM item WHERE itemID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm món ăn: " + e.getMessage(), e);
        }
        return null;
    }

    public List<ItemDTO> getByType(int typeId) throws DatabaseException {
        List<ItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item WHERE typeID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc món ăn theo danh mục: " + e.getMessage(), e);
        }
        return list;
    }

    public List<ItemDTO> getAvailable() throws DatabaseException {
        List<ItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item WHERE status = 'available'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách món ăn đang phục vụ: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean add(ItemDTO item) throws DatabaseException {
        String sql = "INSERT INTO item (itemName, itemPrice, itemDetail, calories, typeID, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getItemPrice());
            ps.setString(3, item.getItemDetail());
            ps.setInt(4, item.getCalories());
            if (item.getTypeId() > 0) ps.setInt(5, item.getTypeId());
            else ps.setNull(5, Types.INTEGER);
            ps.setString(6, item.getStatus());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) item.setItemId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm món ăn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(ItemDTO item) throws DatabaseException {
        String sql = "UPDATE item SET itemName=?, itemPrice=?, itemDetail=?, calories=?, typeID=?, status=? WHERE itemID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getItemPrice());
            ps.setString(3, item.getItemDetail());
            ps.setInt(4, item.getCalories());
            if (item.getTypeId() > 0) ps.setInt(5, item.getTypeId());
            else ps.setNull(5, Types.INTEGER);
            ps.setString(6, item.getStatus());
            ps.setInt(7, item.getItemId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật món ăn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM item WHERE itemID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa món ăn (Do món ăn đã nằm trong hóa đơn): " + e.getMessage(), e);
        }
    }

    @Override
    public List<ItemDTO> search(String keyword) throws DatabaseException {
        List<ItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM item WHERE itemName LIKE ? OR itemDetail LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm món ăn: " + e.getMessage(), e);
        }
        return list;
    }

    private ItemDTO mapRow(ResultSet rs) throws SQLException {
        return new ItemDTO(
            rs.getInt("itemID"),
            rs.getString("itemName"),
            rs.getDouble("itemPrice"),
            rs.getString("itemDetail"),
            rs.getInt("calories"),
            rs.getInt("typeID"),
            rs.getString("status")
        );
    }
}
