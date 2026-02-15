package com.complaintsystem.model;

public class ComplaintCategory {
    private int id;
    private String name;
    private Department department;

    // Transient field/method for backward compatibility if needed, 
    // but better to access via department object.
    // For now we expose getDepartmentId() for easier migration without breaking JSP logic immediately if used.
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

