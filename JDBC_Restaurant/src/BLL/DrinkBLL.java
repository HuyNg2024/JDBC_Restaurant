package BLL;

import DAL.DrinkDAO;
import DTO.DrinkDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class DrinkBLL {
    private final DrinkDAO drinkDAO = new DrinkDAO();

    public List<DrinkDTO> getAllDrinks() throws DatabaseException {
        return drinkDAO.getAll();
    }

    public DrinkDTO getDrinkById(int id) throws DatabaseException {
        return drinkDAO.getById(id);
    }

    public List<DrinkDTO> getDrinksByCategory(String category) throws DatabaseException {
        return drinkDAO.getByCategory(category);
    }

    public List<DrinkDTO> getAvailableDrinks() throws DatabaseException {
        return drinkDAO.getAvailable();
    }

    public boolean addDrink(DrinkDTO drink) throws DatabaseException, ValidationException {
        validateDrink(drink);
        return drinkDAO.add(drink);
    }

    public boolean updateDrink(DrinkDTO drink) throws DatabaseException, ValidationException {
        validateDrink(drink);
        return drinkDAO.update(drink);
    }

    public boolean deleteDrink(int id) throws DatabaseException {
        return drinkDAO.delete(id);
    }

    public List<DrinkDTO> searchDrinks(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllDrinks();
        return drinkDAO.search(keyword.trim());
    }

    private void validateDrink(DrinkDTO drink) throws ValidationException {
        Validator.validateRequired(drink.getDrinkName(), "Tên đồ uống");
        
        if (drink.getDrinkPrice() < 0) {
            throw new ValidationException("Giá bán", "Giá bán không được là số âm!");
        }

        Validator.validateRequired(drink.getCategory(), "Danh mục");
    }
}
