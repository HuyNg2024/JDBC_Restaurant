package DTO;

public class OrderItemDTO {

    private int orderID;
    private int itemID;
    private int quantity;

    // Constructors
    public OrderItemDTO() {
    }

    public OrderItemDTO(int orderID, int itemID, int quantity) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString override
    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "orderID=" + orderID +
                ", itemID=" + itemID +
                ", quantity=" + quantity +
                '}';
    }
}
