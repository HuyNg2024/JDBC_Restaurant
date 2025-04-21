/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.AccountDAO;
import DTO.AccountDTO;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class AccountBLL {
    private AccountDAO accountDAO;
    
    public AccountBLL(){
        accountDAO = new AccountDAO();
    }
    
    public ArrayList<AccountDTO> getAllAccount(){
        return accountDAO.getAllAccount();
    }
    
    public boolean addAccount(AccountDTO account){
        return accountDAO.addAccount(account);
    }
    public boolean updateAccount(AccountDTO account){
        return accountDAO.updateAccount(account);
    }
    
    public boolean deleteAccount(int accountID){
        return accountDAO.deleteAccount(accountID);
    }
    
    public AccountDTO getStaffById(int AccountID){
        return accountDAO.getAccountByID(AccountID);
    }
    
    public AccountDTO checkLogin(String username, String password) {
        AccountDTO account = accountDAO.getAccountByUsername(username);

        if (account != null) {
            if (account.getPassword().equals(password)) {
                return account; // Đăng nhập thành công
            }
        }
        return null; // Sai username hoặc password
    }
}
