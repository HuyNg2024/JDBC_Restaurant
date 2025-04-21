package BLL;

import DAL.CustomerDAO;
import DTO.CustomerDTO;
import java.util.ArrayList;

public class CustomerBLL {
    private CustomerDAO customerDAO;

    public CustomerBLL() {
        customerDAO = new CustomerDAO();
    }

    // Lấy toàn bộ khách hàng
    public ArrayList<CustomerDTO> getAllCustomer() {
        return customerDAO.getAllCustomer();
    }

    // Thêm khách hàng mới
    public boolean addCustomer(CustomerDTO customer) {
        return customerDAO.addCustomer(customer);
    }

    // Cập nhật thông tin khách hàng
    public boolean updateCustomer(CustomerDTO customer) {
        return customerDAO.updateCustomer(customer);
    }

    // Xóa khách hàng theo ID
    public boolean deleteCustomer(int customerID) {
        return customerDAO.deleteCustomer(customerID);
    }

    // Lấy khách hàng theo ID
    public CustomerDTO getCustomerById(int customerID) {
        return customerDAO.getCustomerById(customerID);
    }

    // Lấy khách hàng theo số điện thoại
    public CustomerDTO getCustomerByPhone(String phone) {
        return customerDAO.getCustomerByPhone(phone);
    }
}
