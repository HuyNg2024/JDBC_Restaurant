package DTO;

public class TableDTO {
    private int tableId;
    private String tableName;
    private String status; // "Trống", "Đang dùng", "Đã đặt"

    public TableDTO(int tableId, String tableName, String status) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.status = status;
    }

    // Getters - Setters
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}