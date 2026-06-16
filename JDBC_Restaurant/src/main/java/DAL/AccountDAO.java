package DAL;

import DTO.AccountDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements GenericDAO<AccountDTO> {

    @Override
    public List<AccountDTO> getAll() throws DatabaseException {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM account ORDER BY accountID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách tài khoản: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public AccountDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM account WHERE accountID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm tài khoản: " + e.getMessage(), e);
        }
        return null;
    }

    public AccountDTO getByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm username: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean add(AccountDTO account) throws DatabaseException {
        String sql = "INSERT INTO account (username, password, salt, role, staffID, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getSalt());
            ps.setString(4, account.getRole());
            if (account.getStaffId() > 0) ps.setInt(5, account.getStaffId());
            else ps.setNull(5, Types.INTEGER);
            ps.setString(6, account.getStatus());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) account.setAccountId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm tài khoản: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(AccountDTO account) throws DatabaseException {
        String sql = "UPDATE account SET role=?, staffID=?, status=? WHERE accountID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getRole());
            if (account.getStaffId() > 0) ps.setInt(2, account.getStaffId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, account.getStatus());
            ps.setInt(4, account.getAccountId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật tài khoản: " + e.getMessage(), e);
        }
    }

    public boolean updatePassword(int accountId, String newHash, String newSalt) throws DatabaseException {
        String sql = "UPDATE account SET password=?, salt=? WHERE accountID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, newSalt);
            ps.setInt(3, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật mật khẩu: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM account WHERE accountID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa tài khoản: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AccountDTO> search(String keyword) throws DatabaseException {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE username LIKE ? OR role LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm tài khoản: " + e.getMessage(), e);
        }
        return list;
    }

    private AccountDTO mapRow(ResultSet rs) throws SQLException {
        return new AccountDTO(
            rs.getInt("accountID"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("salt"),
            rs.getString("role"),
            rs.getInt("staffID"),
            rs.getString("status")
        );
    }
}
