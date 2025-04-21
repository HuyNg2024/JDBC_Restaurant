package DTO;

import java.time.LocalDateTime;

public class ReserveDTO {

    private int reserveID;
    private LocalDateTime reserveTime;
    private String customerName;
    private String tableType;
    private Integer tableID;

    // Constructors
    public ReserveDTO() {
    }

    public ReserveDTO(int reserveID, LocalDateTime reserveTime, String customerName, String tableType, Integer tableID) {
        this.reserveID = reserveID;
        this.reserveTime = reserveTime;
        this.customerName = customerName;
        this.tableType = tableType;
        this.tableID = tableID;
    }

    // Getters and Setters
    public int getReserveID() {
        return reserveID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }

    public LocalDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(LocalDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Integer getTableID() {
        return tableID;
    }

    public void setTableID(Integer tableID) {
        this.tableID = tableID;
    }

    // toString override
    @Override
    public String toString() {
        return "ReserveDTO{" +
                "reserveID=" + reserveID +
                ", reserveTime=" + reserveTime +
                ", customerName='" + customerName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", tableID=" + tableID +
                '}';
    }
}
