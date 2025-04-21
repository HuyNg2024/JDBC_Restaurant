package DTO;

public class ItemDTO {

    private int itemID;
    private String itemName;
    private int staffID;
    private String itemDetail;
    private int productMin;
    private double itemPrice;
    private int calories;
    private int itemType;

    // Constructors
    public ItemDTO() {
    }

    public ItemDTO(int itemID, String itemName, int staffID, String itemDetail, int productMin, double itemPrice, int calories, int itemType) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.staffID = staffID;
        this.itemDetail = itemDetail;
        this.productMin = productMin;
        this.itemPrice = itemPrice;
        this.calories = calories;
        this.itemType = itemType;
    }

    // Getters and Setters
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public int getProductMin() {
        return productMin;
    }

    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    // toString (optional)
    @Override
    public String toString() {
        return "ItemDTO{" +
                "itemID=" + itemID +
                ", itemName='" + itemName + '\'' +
                ", staffID=" + staffID +
                ", itemDetail='" + itemDetail + '\'' +
                ", productMin=" + productMin +
                ", itemPrice=" + itemPrice +
                ", calories=" + calories +
                ", itemType=" + itemType +
                '}';
    }
}
