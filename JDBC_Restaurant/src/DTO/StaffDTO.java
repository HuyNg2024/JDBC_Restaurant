/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class StaffDTO {
        
        private int staffID;
        private String staffName;
        private double salary;
        private int workYears;
        private String job;

        public StaffDTO() {
        }

        public StaffDTO(int staffID, String staffName, double Salary, int workYears, String Job) {
            this.staffID = staffID;
            this.staffName = staffName;
            this.salary = Salary;
            this.workYears = workYears;
            this.job = Job;
        }


        public int getStaffID() {
            return staffID;
        }

        public void setStaffID(int staffID) {
            this.staffID = staffID;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public int getWorkYears() {
            return workYears;
        }

        public void setWorkYears(int workYears) {
            this.workYears = workYears;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }
        
        @Override
        public String toString() {
            return "StaffDTO{" +
                    "staffID=" + staffID +
                    ", staffName='" + staffName + '\'' +
                    ", salary=" + salary +
                    ", workYears=" + workYears +
                    ", job='" + job + '\'' +
                    '}';
        }

    }
