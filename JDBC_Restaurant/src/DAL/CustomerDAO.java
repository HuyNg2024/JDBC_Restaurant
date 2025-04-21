/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import DTO.CustomerDTO;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author PC
 */
public class CustomerDAO {
    public ArrayList<CustomerDTO> getAllCustomer(){
        ArrayList<CustomerDTO> customerList = new ArrayList<>();
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            String sql = "SELECT * FROM customer";
            try(PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    CustomerDTO customer = new CustomerDTO(
                        rs.getInt("CustomerID"), 
                        rs.getString("FirstName"), 
                        rs.getString("LastName"), 
                        rs.getString("Phone")
                );
                customerList.add(customer);
                }
            } catch (SQLException e){
                System.err.println("Loi ket noi co so du lieu" + e.getMessage());
            }
            
        }
        return customerList;
    }
    //CREATE
    public boolean addCustomer(CustomerDTO customer){
        Connection conn = DBConnect.getConnection();
        if(conn != null){
            try {
                String sql = "INSERT INTO customer(CustomerID, FirstName, LastName, Phone) VALUES (?, ?, ?, ?)";
                int rows;
                try(PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setInt(1, customer.getCustomerID());
                    stmt.setString(2, customer.getFirstName());
                    stmt.setString(3, customer.getLastName());
                    stmt.setString(4, customer.getPhone());
                    
                    rows = stmt.executeUpdate();
                }
                conn.close();
                return rows > 0 ;
            } catch (SQLException e) {
                System.out.println("Loi ket noi co so du lieu" + e.getMessage());
            }
        }
        return false;
    }
    
   //UPDATE
    public boolean updateCustomer(CustomerDTO customer){
        Connection conn = DBConnect.getConnection();
        if(conn != null){
            try
            {
                String sql = "UPDATE customer SET FirstName=?, LastName=?, Phone=? WHERE CustomerID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setString(1, customer.getFirstName());
                    stmt.setString(2, customer.getLastName());
                    stmt.setString(3, customer.getPhone());
                    stmt.setInt(4, customer.getCustomerID());
                    rows = stmt.executeUpdate();
                }
                conn.close();
                return rows > 0;
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
    
    //  DELETE 
    public boolean deleteCustomer(int customerID) {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            try {
                String sql = "DELETE FROM customer WHERE CustomerID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, customerID);
                    rows = stmt.executeUpdate();
                }
                conn.close();

                return rows > 0;
            } catch (SQLException e) {
                System.out.println("❌ StaffDAO - Lỗi khi xóa: " + e.getMessage());
            }
        }
        return false;
    }
    
    public CustomerDTO getCustomerById(int CustomerID){
        Connection conn = DBConnect.getConnection();
        String sql = "SELECT * FROM customer WHERE CustomerID = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, CustomerID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                CustomerDTO customer = new CustomerDTO(
                        rs.getInt("CustomerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Phone")
                );
                return customer;
            }
        } catch(SQLException e){
            System.out.println(" Lỗi khi lấy nhân viên theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public CustomerDTO getCustomerByPhone(String phone) {   
        Connection conn = DBConnect.getConnection();
        String sql = "SELECT * FROM customer WHERE Phone = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CustomerDTO customer = new CustomerDTO(
                    rs.getInt("CustomerID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Phone")
                );
                return customer;
            }
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm tài khoản theo username: " + e.getMessage());
        }
        return null;
    }
}
