/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class DrinkDTO {

    private int drinkID;
    private String drinkName;
    private double drinkPrice;

    // Constructors
    public DrinkDTO() {
    }

    public DrinkDTO(int drinkID, String drinkName, double drinkPrice) {
        this.drinkID = drinkID;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
    }

    // Getters and Setters
    public int getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(int drinkID) {
        this.drinkID = drinkID;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public double getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(double drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    // toString method (optional)
    @Override
    public String toString() {
        return "DrinkDTO{" +
                "drinkID=" + drinkID +
                ", drinkName='" + drinkName + '\'' +
                ", drinkPrice=" + drinkPrice +
                '}';
    }
}
