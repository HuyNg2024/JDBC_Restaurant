package BLL;

import DAL.StaffDAO;
import DTO.StaffDTO;
import java.util.ArrayList;

public class StaffBLL {
    private  StaffDAO staffDAO;

    public StaffBLL() {
        staffDAO = new StaffDAO();
    }
    
    public ArrayList<StaffDTO> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    public boolean addStaff(StaffDTO staff) {
        return staffDAO.addStaff(staff);
    }
    
    public boolean deleteStaff(int id){
        return staffDAO.deleteStaff(id);
    }
    
    public void updateStaff(StaffDTO staff){
        staffDAO.updateStaff(staff);
    }
    
    public StaffDTO getStaffById(int staffID){
        return staffDAO.getStaffById(staffID);
    }
}
