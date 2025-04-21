package BLL;

import DAL.ItemTypeDAL;
import DTO.ItemTypeDTO;

import java.util.ArrayList;

public class ItemTypeBLL {
    private ItemTypeDAL dal = new ItemTypeDAL();

    public ArrayList<ItemTypeDTO> getAllTypes() {
        return dal.getAllTypes();
    }
}
