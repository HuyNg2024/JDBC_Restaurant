package DAL;

import DTO.StaffDTO;
import exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO implements GenericDAO<StaffDTO> {

    @Override
    public List<StaffDTO> getAll() throws DatabaseException {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staffID DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lấy danh sách nhân viên: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public StaffDTO getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM staff WHERE staffID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm nhân viên: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean add(StaffDTO staff) throws DatabaseException {
        String sql = "INSERT INTO staff (staffName, phone, salary, workYears, job, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, staff.getStaffName());
            ps.setString(2, staff.getPhone());
            ps.setDouble(3, staff.getSalary());
            ps.setInt(4, staff.getWorkYears());
            ps.setString(5, staff.getJob());
            ps.setString(6, staff.getStatus());
            int affected = ps.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) staff.setStaffId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thêm nhân viên: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(StaffDTO staff) throws DatabaseException {
        String sql = "UPDATE staff SET staffName=?, phone=?, salary=?, workYears=?, job=?, status=? WHERE staffID=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getStaffName());
            ps.setString(2, staff.getPhone());
            ps.setDouble(3, staff.getSalary());
            ps.setInt(4, staff.getWorkYears());
            ps.setString(5, staff.getJob());
            ps.setString(6, staff.getStatus());
            ps.setInt(7, staff.getStaffId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi cập nhật nhân viên: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String sql = "DELETE FROM staff WHERE staffID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa nhân viên: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StaffDTO> search(String keyword) throws DatabaseException {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE staffName LIKE ? OR phone LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi tìm kiếm nhân viên: " + e.getMessage(), e);
        }
        return list;
    }

    public List<StaffDTO> getByJob(String job) throws DatabaseException {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE job = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, job);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi lọc nhân viên theo công việc: " + e.getMessage(), e);
        }
        return list;
    }

    private StaffDTO mapRow(ResultSet rs) throws SQLException {
        return new StaffDTO(
            rs.getInt("staffID"),
            rs.getString("staffName"),
            rs.getString("phone"),
            rs.getDouble("salary"),
            rs.getInt("workYears"),
            rs.getString("job"),
            rs.getString("status")
        );
    }
}
