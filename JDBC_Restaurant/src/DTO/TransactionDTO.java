package DTO;

import java.util.Date;

public class TransactionDTO {

    private int transactionID;
    private Integer customerID;
    private String type;
    private String status;
    private Date createAt;
    private Integer orderID;
    private String content;

    // Constructors
    public TransactionDTO() {
    }

    public TransactionDTO(int transactionID, Integer customerID, String type, String status, Date createAt, Integer orderID, String content) {
        this.transactionID = transactionID;
        this.customerID = customerID;
        this.type = type;
        this.status = status;
        this.createAt = createAt;
        this.orderID = orderID;
        this.content = content;
    }

    // Getters and Setters
    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // toString override
    @Override
    public String toString() {
        return "TransactionDTO{" +
                "transactionID=" + transactionID +
                ", customerID=" + customerID +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createAt=" + createAt +
                ", orderID=" + orderID +
                ", content='" + content + '\'' +
                '}';
    }
}
