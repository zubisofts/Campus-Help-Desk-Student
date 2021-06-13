package com.zubisoft.campushelpdeskstudent.models;

public class UserModel {

    private String id;
    private String fullName;
    private String regNo;
    private String staffNo;
    private String level;
    private String department;
    private String type;
    private String email;
    private String phoneNumber;
    private long createdAt;

    public UserModel() {
    }

    public UserModel(String fullName, String regNo, String staffNo, String level, String department, String type, String email, String phoneNumber, long createdAt) {
        this.fullName = fullName;
        this.regNo = regNo;
        this.staffNo = staffNo;
        this.level = level;
        this.department = department;
        this.type = type;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
