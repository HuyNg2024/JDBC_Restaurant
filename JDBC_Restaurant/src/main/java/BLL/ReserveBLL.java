package BLL;

import DAL.ReserveDAO;
import DTO.ReserveDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.time.LocalDate;
import java.util.List;

public class ReserveBLL {
    private final ReserveDAO reserveDAO = new ReserveDAO();

    public List<ReserveDTO> getAllReservations() throws DatabaseException {
        return reserveDAO.getAll();
    }

    public ReserveDTO getReservationById(int id) throws DatabaseException {
        return reserveDAO.getById(id);
    }

    public List<ReserveDTO> getReservationsByCustomer(int customerId) throws DatabaseException {
        return reserveDAO.getByCustomer(customerId);
    }

    public List<ReserveDTO> getReservationsByDate(LocalDate date) throws DatabaseException {
        return reserveDAO.getByDate(date);
    }

    public boolean addReservation(ReserveDTO reserve) throws DatabaseException, ValidationException {
        validateReservation(reserve);
        return reserveDAO.add(reserve);
    }

    public boolean updateReservation(ReserveDTO reserve) throws DatabaseException, ValidationException {
        validateReservation(reserve);
        return reserveDAO.update(reserve);
    }

    public boolean deleteReservation(int id) throws DatabaseException {
        return reserveDAO.delete(id);
    }

    public List<ReserveDTO> searchReservations(String keyword) throws DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) return getAllReservations();
        return reserveDAO.search(keyword.trim());
    }

    private void validateReservation(ReserveDTO reserve) throws ValidationException {
        if (reserve.getCustomerId() <= 0) {
            throw new ValidationException("Khách hàng", "Vui lòng chọn khách hàng!");
        }

        if (reserve.getTableId() <= 0) {
            throw new ValidationException("Bàn ăn", "Vui lòng chọn bàn ăn!");
        }

        if (reserve.getReserveTime() == null) {
            throw new ValidationException("Thời gian", "Vui lòng chọn thời gian đặt bàn!");
        }

        if (reserve.getGuestCount() <= 0) {
            throw new ValidationException("Số khách", "Số lượng khách phải lớn hơn 0!");
        }
    }
}
