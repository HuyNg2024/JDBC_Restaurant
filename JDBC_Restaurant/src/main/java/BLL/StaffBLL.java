package BLL;

import DAL.StaffDAO;
import DTO.StaffDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class StaffBLL {
    private final StaffDAO staffDAO = new StaffDAO();

    public List<StaffDTO> getAllStaff() throws DatabaseException {
        return staffDAO.getAll();
    }

    public StaffDTO getStaffById(int id) throws DatabaseException {
        return staffDAO.getById(id);
    }

    public boolean addStaff(StaffDTO staff) throws DatabaseException, ValidationException {
        validateStaff(staff);
        return staffDAO.add(staff);
    }

    public boolean updateStaff(StaffDTO staff) throws DatabaseException, ValidationException {
        validateStaff(staff);
        return staffDAO.update(staff);
    }

    public boolean deleteStaff(int id) throws DatabaseException {
        return staffDAO.delete(id);
    }

    public List<StaffDTO> searchStaff(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllStaff();
        return staffDAO.search(keyword.trim());
    }

    public List<StaffDTO> getByJob(String job) throws DatabaseException {
        return staffDAO.getByJob(job);
    }

    private void validateStaff(StaffDTO staff) throws ValidationException {
        Validator.validateRequired(staff.getStaffName(), "Tên nhân viên");
        
        if (!Validator.isNullOrEmpty(staff.getPhone())) {
            Validator.validatePhone(staff.getPhone(), "Số điện thoại");
        }

        if (staff.getSalary() < 0) {
            throw new ValidationException("Lương", "Lương không được nhỏ hơn 0!");
        }

        if (staff.getWorkYears() < 0) {
            throw new ValidationException("Số năm kinh nghiệm", "Kinh nghiệm không được là số âm!");
        }

        Validator.validateRequired(staff.getJob(), "Công việc");
    }
}
