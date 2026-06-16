package BLL;

import DAL.TabletopDAO;
import DTO.TabletopDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class TabletopBLL {
    private final TabletopDAO tableDAO = new TabletopDAO();

    public List<TabletopDTO> getAllTables() throws DatabaseException {
        return tableDAO.getAll();
    }

    public TabletopDTO getTableById(int id) throws DatabaseException {
        return tableDAO.getById(id);
    }

    public List<TabletopDTO> getTablesByStatus(String status) throws DatabaseException {
        return tableDAO.getByStatus(status);
    }

    public boolean addTable(TabletopDTO table) throws DatabaseException, ValidationException {
        validateTable(table);
        return tableDAO.add(table);
    }

    public boolean updateTable(TabletopDTO table) throws DatabaseException, ValidationException {
        validateTable(table);
        return tableDAO.update(table);
    }

    public boolean updateStatus(int tableId, String status) throws DatabaseException {
        return tableDAO.updateStatus(tableId, status);
    }

    public boolean deleteTable(int id) throws DatabaseException {
        return tableDAO.delete(id);
    }

    public List<TabletopDTO> searchTables(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllTables();
        return tableDAO.search(keyword.trim());
    }

    private void validateTable(TabletopDTO table) throws ValidationException {
        Validator.validateRequired(table.getTableCode(), "Mã bàn");
        
        if (table.getCapacity() <= 0) {
            throw new ValidationException("Sức chứa", "Sức chứa phải lớn hơn 0!");
        }

        if (table.getFloor() <= 0) {
            throw new ValidationException("Tầng", "Tầng phải là số dương!");
        }
    }
}
