package DTO;

public class TabletopDTO {

    private int tableID;
    private Integer customerID;
    private String tableCode;
    private String status;
    private Integer capacity;
    private Integer current;

    // Constructors
    public TabletopDTO() {
    }

    public TabletopDTO(int tableID, Integer customerID, String tableCode, String status, Integer capacity, Integer current) {
        this.tableID = tableID;
        this.customerID = customerID;
        this.tableCode = tableCode;
        this.status = status;
        this.capacity = capacity;
        this.current = current;
    }

    // Getters and Setters
    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    // toString override
    @Override
    public String toString() {
        return "TabletopDTO{" +
                "tableID=" + tableID +
                ", customerID=" + customerID +
                ", tableCode='" + tableCode + '\'' +
                ", status='" + status + '\'' +
                ", capacity=" + capacity +
                ", current=" + current +
                '}';
    }
}
