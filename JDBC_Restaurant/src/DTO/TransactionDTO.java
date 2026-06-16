package DTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction entity.
 */
public class TransactionDTO {
    private int transactionId;
    private int orderId;
    private int customerId;
    private double amount;
    private String paymentMethod;
    private String status;
    private String note;
    private LocalDateTime createdAt;

    public TransactionDTO() {}

    public TransactionDTO(int transactionId, int orderId, int customerId, double amount, String paymentMethod, String status, String note) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.note = note;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Transaction #" + transactionId;
    }
}
