package DTO;

/**
 * Data Transfer Object for Account entity.
 */
public class AccountDTO {
    private int accountId;
    private String username;
    private String password;
    private String salt;
    private String role;
    private int staffId;
    private String status;

    public AccountDTO() {}

    public AccountDTO(int accountId, String username, String password, String salt, String role, int staffId, String status) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.staffId = staffId;
        this.status = status;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return username + " [" + role + "]";
    }
}
