package DTO;

/**
 * Data Transfer Object for Item entity.
 */
public class ItemDTO {
    private int itemId;
    private String itemName;
    private double itemPrice;
    private String itemDetail;
    private int calories;
    private int typeId;
    private String status;

    public ItemDTO() {}

    public ItemDTO(int itemId, String itemName, double itemPrice, String itemDetail, int calories, int typeId, String status) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDetail = itemDetail;
        this.calories = calories;
        this.typeId = typeId;
        this.status = status;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getItemPrice() { return itemPrice; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }

    public String getItemDetail() { return itemDetail; }
    public void setItemDetail(String itemDetail) { this.itemDetail = itemDetail; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return itemName;
    }
}
