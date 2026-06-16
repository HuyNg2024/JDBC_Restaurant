package BLL;

import DAL.ItemTypeDAO;
import DTO.ItemTypeDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class ItemTypeBLL {
    private final ItemTypeDAO itemTypeDAO = new ItemTypeDAO();

    public List<ItemTypeDTO> getAllItemTypes() throws DatabaseException {
        return itemTypeDAO.getAll();
    }

    public ItemTypeDTO getItemTypeById(int id) throws DatabaseException {
        return itemTypeDAO.getById(id);
    }

    public boolean addItemType(ItemTypeDTO itemType) throws DatabaseException, ValidationException {
        validateItemType(itemType);
        return itemTypeDAO.add(itemType);
    }

    public boolean updateItemType(ItemTypeDTO itemType) throws DatabaseException, ValidationException {
        validateItemType(itemType);
        return itemTypeDAO.update(itemType);
    }

    public boolean deleteItemType(int id) throws DatabaseException {
        return itemTypeDAO.delete(id);
    }

    public List<ItemTypeDTO> searchItemTypes(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllItemTypes();
        return itemTypeDAO.search(keyword.trim());
    }

    private void validateItemType(ItemTypeDTO itemType) throws ValidationException {
        Validator.validateRequired(itemType.getTypeName(), "Tên danh mục");
    }
}
