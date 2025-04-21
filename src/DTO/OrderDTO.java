package DTO;

import java.math.BigDecimal;

public class OrderDTO {

    private int orderID;
    private Integer tableID;
    private Integer customerID;
    private BigDecimal totalPrice;
    private String content;
    private Integer transactionID;

    // Constructors
    public OrderDTO() {
    }

    public OrderDTO(int orderID, Integer tableID, Integer customerID, BigDecimal totalPrice, String content, Integer transactionID) {
        this.orderID = orderID;
        this.tableID = tableID;
        this.customerID = customerID;
        this.totalPrice = totalPrice;
        this.content = content;
        this.transactionID = transactionID;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Integer getTableID() {
        return tableID;
    }

    public void setTableID(Integer tableID) {
        this.tableID = tableID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    // toString override
    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderID=" + orderID +
                ", tableID=" + tableID +
                ", customerID=" + customerID +
                ", totalPrice=" + totalPrice +
                ", content='" + content + '\'' +
                ", transactionID=" + transactionID +
                '}';
    }
}
