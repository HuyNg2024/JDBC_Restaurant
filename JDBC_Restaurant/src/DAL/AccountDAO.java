/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import DTO.AccountDTO;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author PC
 */
public class AccountDAO {
    public ArrayList<AccountDTO> getAllAccount(){
        ArrayList<AccountDTO> accountList = new ArrayList<>();
        Connection conn = DBConnect.getConnection();
        
        if(conn != null){
            String sql = "SELECT * FROM account";
            try(PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    AccountDTO account = new AccountDTO(
                            rs.getInt("accountID"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getInt("staffID")
                    );
                    accountList.add(account);
                }
                conn.close();
            } catch(SQLException e){
                System.out.println("Loi khi lay du lieu" + e.getMessage()); 
            }
                    
        }
        return accountList;
    }
    
    //CREATE
    public boolean addAccount(AccountDTO account){
        Connection conn = DBConnect.getConnection();
        if(conn != null){
            try {
                String sql = "INSERT INTO account(accountID, username, password, role, staffID) VALUES (?, ?, ?, ?, ?)";
                int rows;
                try(PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setInt(1, account.getAccountID());
                    stmt.setString(2, account.getUsername());
                    stmt.setString(3, account.getPassword());
                    stmt.setString(4, account.getRole());
                    stmt.setInt(5, account.getStaffID());
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
    public boolean updateAccount(AccountDTO account){
        Connection conn = DBConnect.getConnection();
        if(conn != null){
            try
            {
                String sql = "UPDATE account SET username=?, password=?, role=?, staffID=? WHERE accountID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setString(1, account.getUsername());
                    stmt.setString(2, account.getPassword());
                    stmt.setString(3, account.getRole());
                    stmt.setInt(4, account.getStaffID());
                    stmt.setInt(5, account.getAccountID());
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
    public boolean deleteAccount(int accountID) {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            try {
                String sql = "DELETE FROM account WHERE accountID=?";
                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, accountID);
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
    
    public AccountDTO getAccountByID(int AccountID){
        Connection conn = DBConnect.getConnection();
        String sql = "SELECT * FROM account WHERE accountID = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, AccountID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                AccountDTO account = new AccountDTO(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("staffID")
                );
                return account;
            }
        } catch(SQLException e){
            System.out.println(" Lỗi khi lấy nhân viên theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public AccountDTO getAccountByUsername(String username) {   
        Connection conn = DBConnect.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AccountDTO account = new AccountDTO(
                    rs.getInt("accountID"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getInt("staffID")
                );
                return account;
            }
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm tài khoản theo username: " + e.getMessage());
        }
        return null;
    }

}
