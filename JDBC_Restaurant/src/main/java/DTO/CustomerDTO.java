package DTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Customer entity.
 */
public class CustomerDTO {
    private int customerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private int totalVisits;
    private LocalDateTime createdAt;

    public CustomerDTO() {}

    public CustomerDTO(int customerId, String firstName, String lastName, String phone, String email, String address, int totalVisits) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.totalVisits = totalVisits;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return lastName + " " + firstName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getTotalVisits() { return totalVisits; }
    public void setTotalVisits(int totalVisits) { this.totalVisits = totalVisits; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return getFullName() + " - " + phone;
    }
}
