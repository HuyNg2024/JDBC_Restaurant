package DTO;

/**
 * Data Transfer Object for Drink entity.
 */
public class DrinkDTO {
    private int drinkId;
    private String drinkName;
    private double drinkPrice;
    private String category;
    private String status;

    public DrinkDTO() {}

    public DrinkDTO(int drinkId, String drinkName, double drinkPrice, String category, String status) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
        this.category = category;
        this.status = status;
    }

    public int getDrinkId() { return drinkId; }
    public void setDrinkId(int drinkId) { this.drinkId = drinkId; }

    public String getDrinkName() { return drinkName; }
    public void setDrinkName(String drinkName) { this.drinkName = drinkName; }

    public double getDrinkPrice() { return drinkPrice; }
    public void setDrinkPrice(double drinkPrice) { this.drinkPrice = drinkPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return drinkName;
    }
}
