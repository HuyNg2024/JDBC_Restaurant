package BLL;

import DAL.AccountDAO;
import DTO.AccountDTO;
import exceptions.AuthenticationException;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.PasswordHasher;
import utils.Validator;
import java.util.List;

public class AccountBLL {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<AccountDTO> getAllAccounts() throws DatabaseException {
        return accountDAO.getAll();
    }

    public AccountDTO getAccountById(int id) throws DatabaseException {
        return accountDAO.getById(id);
    }

    public boolean addAccount(AccountDTO account) throws DatabaseException, ValidationException {
        Validator.validateRequired(account.getUsername(), "Tài khoản");
        Validator.validateRequired(account.getPassword(), "Mật khẩu");
        
        if (account.getPassword().length() < 6) {
            throw new ValidationException("Mật khẩu", "Mật khẩu phải có ít nhất 6 ký tự!");
        }
        
        // Check if username exists
        if (accountDAO.getByUsername(account.getUsername()) != null) {
            throw new ValidationException("Tài khoản", "Tài khoản đã tồn tại!");
        }

        // Generate salt and hash password
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hash(account.getPassword(), salt);
        account.setSalt(salt);
        account.setPassword(hash);

        return accountDAO.add(account);
    }

    public boolean updateAccount(AccountDTO account) throws DatabaseException, ValidationException {
        return accountDAO.update(account);
    }

    public boolean deleteAccount(int id) throws DatabaseException {
        return accountDAO.delete(id);
    }

    public AccountDTO checkLogin(String username, String password) throws DatabaseException, AuthenticationException {
        if (Validator.isNullOrEmpty(username) || Validator.isNullOrEmpty(password)) {
            throw new AuthenticationException("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
        }

        AccountDTO account = accountDAO.getByUsername(username);
        if (account == null) {
            throw new AuthenticationException("Tài khoản không tồn tại!");
        }

        if ("inactive".equals(account.getStatus())) {
            throw new AuthenticationException("Tài khoản đã bị khóa!");
        }

        // Verify password
        boolean isMatch = PasswordHasher.verify(password, account.getPassword(), account.getSalt());
        if (!isMatch) {
            throw new AuthenticationException("Mật khẩu không chính xác!");
        }

        return account;
    }

    public boolean changePassword(int accountId, String oldPassword, String newPassword) 
            throws DatabaseException, AuthenticationException, ValidationException {
        
        AccountDTO account = accountDAO.getById(accountId);
        if (account == null) throw new DatabaseException("Tài khoản không tồn tại!");

        // Verify old password
        if (!PasswordHasher.verify(oldPassword, account.getPassword(), account.getSalt())) {
            throw new AuthenticationException("Mật khẩu cũ không chính xác!");
        }

        Validator.validateRequired(newPassword, "Mật khẩu mới");
        if (newPassword.length() < 6) {
            throw new ValidationException("Mật khẩu mới", "Mật khẩu phải có ít nhất 6 ký tự!");
        }

        // Hash new password
        String newSalt = PasswordHasher.generateSalt();
        String newHash = PasswordHasher.hash(newPassword, newSalt);

        return accountDAO.updatePassword(accountId, newHash, newSalt);
    }

    public List<AccountDTO> searchAccounts(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllAccounts();
        return accountDAO.search(keyword.trim());
    }
}
