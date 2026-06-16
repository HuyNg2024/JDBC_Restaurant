package DAL;

import DTO.DrinkDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO implements GenericDAO<DrinkDTO> {

    @Override
    public List<DrinkDTO> getAll() throws DatabaseException {
        List<DrinkDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM drink ORDER BY drinkID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách đồ uống: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DrinkDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM drink WHERE drinkID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm đồ uống: " + e.getMessage(), e);
        }
        return null;
    }

    public List<DrinkDTO> getByCategory(String category) throws DatabaseException {
        List<DrinkDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM drink WHERE category = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc đồ uống theo danh mục: " + e.getMessage(), e);
        }
        return list;
    }

    public List<DrinkDTO> getAvailable() throws DatabaseException {
        List<DrinkDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM drink WHERE status = 'available'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách đồ uống đang phục vụ: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean add(DrinkDTO drink) throws DatabaseException {
        String sql = "INSERT INTO drink (drinkName, drinkPrice, category, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, drink.getDrinkName());
            ps.setDouble(2, drink.getDrinkPrice());
            ps.setString(3, drink.getCategory());
            ps.setString(4, drink.getStatus());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) drink.setDrinkId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm đồ uống: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(DrinkDTO drink) throws DatabaseException {
        String sql = "UPDATE drink SET drinkName=?, drinkPrice=?, category=?, status=? WHERE drinkID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drink.getDrinkName());
            ps.setDouble(2, drink.getDrinkPrice());
            ps.setString(3, drink.getCategory());
            ps.setString(4, drink.getStatus());
            ps.setInt(5, drink.getDrinkId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật đồ uống: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM drink WHERE drinkID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa đồ uống: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DrinkDTO> search(String keyword) throws DatabaseException {
        List<DrinkDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM drink WHERE drinkName LIKE ? OR category LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm đồ uống: " + e.getMessage(), e);
        }
        return list;
    }

    private DrinkDTO mapRow(ResultSet rs) throws SQLException {
        return new DrinkDTO(
            rs.getInt("drinkID"),
            rs.getString("drinkName"),
            rs.getDouble("drinkPrice"),
            rs.getString("category"),
            rs.getString("status")
        );
    }
}
