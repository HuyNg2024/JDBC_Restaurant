package DTO;

/**
 * Data Transfer Object for Staff entity.
 */
public class StaffDTO {
    private int staffId;
    private String staffName;
    private String phone;
    private double salary;
    private int workYears;
    private String job;
    private String status;

    public StaffDTO() {}

    public StaffDTO(int staffId, String staffName, String phone, double salary, int workYears, String job, String status) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.phone = phone;
        this.salary = salary;
        this.workYears = workYears;
        this.job = job;
        this.status = status;
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public int getWorkYears() { return workYears; }
    public void setWorkYears(int workYears) { this.workYears = workYears; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return staffName + " (" + job + ")";
    }
}
