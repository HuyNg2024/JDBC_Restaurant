package BLL;

import DAL.TabletopDAL;
import DTO.TabletopDTO;

import java.util.ArrayList;

public class TabletopBLL {
    private TabletopDAL tabletopDAL = new TabletopDAL();

    public ArrayList<TabletopDTO> getAllTables() {
        return tabletopDAL.getAllTables();
    }

    public boolean addTable(TabletopDTO table) {
        return tabletopDAL.insertTable(table);
    }

    public boolean updateTable(TabletopDTO table) {
        return tabletopDAL.updateTable(table);
    }

    public boolean deleteTable(int tableID) {
        return tabletopDAL.deleteTable(tableID);
    }
}
