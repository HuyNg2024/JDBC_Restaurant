package BLL;

import DAL.ItemDAL;
import DTO.ItemDTO;

import java.util.ArrayList;

public class ItemBLL {
    private ItemDAL itemDAL = new ItemDAL();

    public ArrayList<ItemDTO> getAllItems() {
        return itemDAL.getAllItems();
    }

    public boolean addItem(ItemDTO item) {
        return itemDAL.insertItem(item);
    }

    public boolean updateItem(ItemDTO item) {
        return itemDAL.updateItem(item);
    }

    public boolean deleteItem(int itemID) {
        return itemDAL.deleteItem(itemID);
    }
}
