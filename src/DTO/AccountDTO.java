/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class AccountDTO {
    private int accountID;
    private String username;
    private String role;
    private int staffID;

    // Constructor
    public AccountDTO(int accountID, String username, String role, int staffID) {
        this.accountID = accountID;
        this.username = username;
        this.role = role;
        this.staffID = staffID;
    }

    // Getters and Setters
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountID=" + accountID +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", staffID=" + staffID +
                '}';
    }
}

