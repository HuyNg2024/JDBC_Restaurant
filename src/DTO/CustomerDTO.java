/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class CustomerDTO {

    private int customerID;
    private String firstName;
    private String lastName;
    private String phone;
    private int staffID;

    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(int customerID, String firstName, String lastName, String phone, int staffID) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.staffID = staffID;
    }

    // Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    // toString method (optional)
    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerID=" + customerID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", staffID=" + staffID +
                '}';
    }
}
