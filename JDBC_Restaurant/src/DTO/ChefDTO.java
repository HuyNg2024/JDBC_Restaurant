/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class ChefDTO {
    private int staffID;
    private String cookType;

    // Constructors
    public ChefDTO() {
    }

    public ChefDTO(int staffID, String cookType) {
        this.staffID = staffID;
        this.cookType = cookType;
    }

    // Getters and Setters
    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getCookType() {
        return cookType;
    }

    public void setCookType(String cookType) {
        this.cookType = cookType;
    }

    // toString method (optional)
    @Override
    public String toString() {
        return "ChefDTO{" +
                "staffID=" + staffID +
                ", cookType='" + cookType + '\'' +
                '}';
    }
}
