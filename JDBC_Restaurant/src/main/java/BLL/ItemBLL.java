package BLL;

import DAL.ItemDAO;
import DTO.ItemDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class ItemBLL {
    private final ItemDAO itemDAO = new ItemDAO();

    public List<ItemDTO> getAllItems() throws DatabaseException {
        return itemDAO.getAll();
    }

    public ItemDTO getItemById(int id) throws DatabaseException {
        return itemDAO.getById(id);
    }

    public List<ItemDTO> getItemsByType(int typeId) throws DatabaseException {
        return itemDAO.getByType(typeId);
    }

    public List<ItemDTO> getAvailableItems() throws DatabaseException {
        return itemDAO.getAvailable();
    }

    public boolean addItem(ItemDTO item) throws DatabaseException, ValidationException {
        validateItem(item);
        return itemDAO.add(item);
    }

    public boolean updateItem(ItemDTO item) throws DatabaseException, ValidationException {
        validateItem(item);
        return itemDAO.update(item);
    }

    public boolean deleteItem(int id) throws DatabaseException {
        return itemDAO.delete(id);
    }

    public List<ItemDTO> searchItems(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllItems();
        return itemDAO.search(keyword.trim());
    }

    private void validateItem(ItemDTO item) throws ValidationException {
        Validator.validateRequired(item.getItemName(), "Tên món ăn");
        
        if (item.getItemPrice() < 0) {
            throw new ValidationException("Giá bán", "Giá bán không được là số âm!");
        }
        
        if (item.getCalories() < 0) {
            throw new ValidationException("Calories", "Calories không được là số âm!");
        }

        if (item.getTypeId() <= 0) {
            throw new ValidationException("Danh mục", "Vui lòng chọn danh mục hợp lệ!");
        }
    }
}
