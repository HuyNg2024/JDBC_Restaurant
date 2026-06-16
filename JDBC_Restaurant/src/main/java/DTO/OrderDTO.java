package DTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Order entity.
 */
public class OrderDTO {
    private int orderId;
    private int tableId;
    private int customerId;
    private double totalPrice;
    private String status;
    private String note;
    private LocalDateTime createdAt;

    public OrderDTO() {}

    public OrderDTO(int orderId, int tableId, int customerId, double totalPrice, String status, String note) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.note = note;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Order #" + orderId;
    }
}
