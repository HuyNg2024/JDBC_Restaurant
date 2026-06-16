package BLL;

import DAL.CustomerDAO;
import DTO.CustomerDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class CustomerBLL {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<CustomerDTO> getAllCustomers() throws DatabaseException {
        return customerDAO.getAll();
    }

    public CustomerDTO getCustomerById(int id) throws DatabaseException {
        return customerDAO.getById(id);
    }
    
    public CustomerDTO getCustomerByPhone(String phone) throws DatabaseException {
        return customerDAO.getByPhone(phone);
    }

    public boolean addCustomer(CustomerDTO customer) throws DatabaseException, ValidationException {
        validateCustomer(customer, true);
        return customerDAO.add(customer);
    }

    public boolean updateCustomer(CustomerDTO customer) throws DatabaseException, ValidationException {
        validateCustomer(customer, false);
        return customerDAO.update(customer);
    }

    public boolean deleteCustomer(int id) throws DatabaseException {
        return customerDAO.delete(id);
    }

    public List<CustomerDTO> searchCustomers(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllCustomers();
        return customerDAO.search(keyword.trim());
    }

    private void validateCustomer(CustomerDTO customer, boolean isAdd) throws ValidationException, DatabaseException {
        Validator.validateRequired(customer.getFirstName(), "Họ");
        Validator.validateRequired(customer.getLastName(), "Tên");
        
        Validator.validatePhone(customer.getPhone(), "Số điện thoại");
        
        // Check for duplicate phone number
        CustomerDTO existing = customerDAO.getByPhone(customer.getPhone());
        if (existing != null) {
            if (isAdd || existing.getCustomerId() != customer.getCustomerId()) {
                throw new ValidationException("Số điện thoại", "Số điện thoại này đã được sử dụng!");
            }
        }

        if (!Validator.isNullOrEmpty(customer.getEmail())) {
            Validator.validateEmail(customer.getEmail(), "Email");
        }
    }
}
