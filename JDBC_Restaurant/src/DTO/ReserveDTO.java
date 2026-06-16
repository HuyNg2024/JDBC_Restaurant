package DTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Reserve entity.
 */
public class ReserveDTO {
    private int reserveId;
    private int customerId;
    private int tableId;
    private LocalDateTime reserveTime;
    private int guestCount;
    private String note;
    private String status;
    private LocalDateTime createdAt;

    public ReserveDTO() {}

    public ReserveDTO(int reserveId, int customerId, int tableId, LocalDateTime reserveTime, int guestCount, String note, String status) {
        this.reserveId = reserveId;
        this.customerId = customerId;
        this.tableId = tableId;
        this.reserveTime = reserveTime;
        this.guestCount = guestCount;
        this.note = note;
        this.status = status;
    }

    public int getReserveId() { return reserveId; }
    public void setReserveId(int reserveId) { this.reserveId = reserveId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public LocalDateTime getReserveTime() { return reserveTime; }
    public void setReserveTime(LocalDateTime reserveTime) { this.reserveTime = reserveTime; }

    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Reserve #" + reserveId + " (" + status + ")";
    }
}
