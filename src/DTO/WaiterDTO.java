package DTO;

public class WaiterDTO {

    private int staffID;

    // Constructors
    public WaiterDTO() {
    }

    public WaiterDTO(int staffID) {
        this.staffID = staffID;
    }

    // Getter and Setter
    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    // toString override
    @Override
    public String toString() {
        return "WaiterDTO{" +
                "staffID=" + staffID +
                '}';
    }
}
