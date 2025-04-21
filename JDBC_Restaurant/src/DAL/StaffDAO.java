package DAL;

import DTO.StaffDTO;
import java.sql.*;
import java.util.ArrayList;

public class StaffDAO {
    

    //READ - Lấy danh sách nhân viên
    public ArrayList<StaffDTO> getAllStaff() {
        ArrayList<StaffDTO> staffList = new ArrayList<>();
        Connection conn = DBConnect.getConnection();

        if (conn != null) {
            try {
                String sql = "SELECT * FROM staff";
                try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        StaffDTO staff = new StaffDTO(
                                rs.getInt("staffID"),
                                rs.getString("staffName"),
                                rs.getDouble("Salary"),
                                rs.getInt("workYears"),
                                rs.getString("Job")
                        );
                        staffList.add(staff);
                    }

                }
                conn.close();
            } catch (SQLException e) {
                System.out.println("❌ StaffDAO - Lỗi khi lấy dữ liệu: " + e.getMessage());
            }
        }

        return staffList;
    }

    //  CREATE 
    public boolean addStaff(StaffDTO staff) {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            try {
                String sql = "INSERT INTO staff(staffID, staffName, salary, workYears, job) VALUES (?, ?, ?, ?, ?)";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, staff.getStaffID());
                    stmt.setString(2, staff.getStaffName());
                    stmt.setDouble(3, staff.getSalary());
                    stmt.setInt(4, staff.getWorkYears());
                    stmt.setString(5, staff.getJob());
                    rows = stmt.executeUpdate();
                }
                conn.close();

                return rows > 0;
            } catch (SQLException e) {
                System.out.println("❌ StaffDAO - Lỗi khi thêm: " + e.getMessage());
            }
        }
        return false;
    }

    //  UPDATE 
    public boolean updateStaff(StaffDTO staff) {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            try {
                String sql = "UPDATE staff SET staffName=?, salary=?, workYears=?, job=? WHERE staffID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, staff.getStaffName());
                    stmt.setDouble(2, staff.getSalary());
                    stmt.setInt(3, staff.getWorkYears());
                    stmt.setString(4, staff.getJob());
                    stmt.setInt(5, staff.getStaffID());
                    rows = stmt.executeUpdate();
                }
                conn.close();

                return rows > 0;
            } catch (SQLException e) {
                System.out.println("❌ StaffDAO - Lỗi khi cập nhật: " + e.getMessage());
            }
        }
        return false;
    }

    //  DELETE 
    public boolean deleteStaff(int staffID) {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            try {
                String sql = "DELETE FROM staff WHERE staffID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, staffID);
                    rows = stmt.executeUpdate();
                }
                conn.close();

                return rows > 0;
            } catch (SQLException e) {
                System.out.println("❌ StaffDAO - Lỗi khi xóa: " + e.getMessage());
            }
        }
        return false;
    }

//    // MAIN để test CRUD
//    public static void main(String[] args) {
//        StaffDAO dao = new StaffDAO();
//
//        //  Test READ
//        System.out.println(" Danh sách nhân viên:");
//        ArrayList<StaffDTO> list = dao.getAllStaff();
//        for (StaffDTO staff : list) {
//            System.out.println(" ID: " + staff.getStaffID()
//                    + " | Tên: " + staff.getStaffName()
//                    + " | Lương: " + staff.getSalary()
//                    + " | Số năm làm việc: " + staff.getWorkYears()
//                    + " | Công việc: " + staff.getJob());
//        }
//
//        // 👇 Test CREATE
//        /*
//        StaffDTO newStaff = new StaffDTO(6, "Trần Bích Hường", 9000000, 3, "Phục vụ");
//        if (dao.addStaff(newStaff)) {
//            System.out.println("✅ Thêm nhân viên mới thành công!");
//        }
//        */
//
//        // 👇 Test UPDATE
//        /*
//        StaffDTO updateStaff = new StaffDTO(6, "Trần Bích Hường", 9500000, 4, "Phục vụ trưởng");
//        if (dao.updateStaff(updateStaff)) {
//            System.out.println("✅ Cập nhật nhân viên thành công!");
//        }
//        */
//
//        // 👇 Test DELETE
//        /*
//        if (dao.deleteStaff(6)) {
//            System.out.println("✅ Xóa nhân viên thành công!");
//        }
//        */
//    }
    
    public StaffDTO getStaffById(int StaffID){
        Connection conn = DBConnect.getConnection();
        String sql = "SELECT * FROM staff WHERE staffID = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, StaffID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                StaffDTO staff = new StaffDTO(
                        rs.getInt("staffID"),
                        rs.getString("staffName"),
                        rs.getDouble("Salary"),
                        rs.getInt("workYears"),
                        rs.getString("Job")
                );
                return staff;
            }
        } catch(SQLException e){
            System.out.println(" Lỗi khi lấy nhân viên theo ID: " + e.getMessage());
        }
        
        return null;
    }
}
