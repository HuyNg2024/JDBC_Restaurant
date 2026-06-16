package DTO;

/**
 * Data Transfer Object for Tabletop entity.
 */
public class TabletopDTO {
    private int tableId;
    private String tableCode;
    private int capacity;
    private String status;
    private int floor;

    public TabletopDTO() {}

    public TabletopDTO(int tableId, String tableCode, int capacity, String status, int floor) {
        this.tableId = tableId;
        this.tableCode = tableCode;
        this.capacity = capacity;
        this.status = status;
        this.floor = floor;
    }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    @Override
    public String toString() {
        return tableCode + " (" + capacity + " người)";
    }
}
