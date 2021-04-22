package com.zubisoft.campushelpdeskstudent.models;

public class Student {

    private String id;
    private String fullName;
    private String regNo;
    private String level;
    private String department;
    private String email;
    private long createdAt;

    public Student() {
    }

    public Student(String fullName, String regNo, String level, String department, String email, long createdAt) {
        this.fullName = fullName;
        this.regNo = regNo;
        this.level = level;
        this.department = department;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
