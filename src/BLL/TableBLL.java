package BLL;

import DAL.TableDAO;
import DTO.TableDTO;
import java.util.*;

public class TableBLL {
    private TableDAO tableDAO = new TableDAO();

    public List<TableDTO> getAllTables() {
        return tableDAO.getAllTables();
    }

    public boolean updateTableStatus(int tableId, String status) {
        return tableDAO.updateStatus(tableId, status);
    }
}