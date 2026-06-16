package DTO;

/**
 * Data Transfer Object for OrderItem entity.
 */
public class OrderItemDTO {
    private int orderId;
    private int itemId;
    private String itemName;
    private int quantity;
    private double unitPrice;

    public OrderItemDTO() {}

    public OrderItemDTO(int orderId, int itemId, String itemName, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    @Override
    public String toString() {
        return itemName + " x" + quantity;
    }
}
