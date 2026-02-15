package com.complaintsystem.model;

import java.util.Date;

public class Complaint {
    private int id;
    private User user;
    private ComplaintCategory category;
    private String title;
    private String description;
    private String priority;      // LOW / MEDIUM / HIGH
    private String status = "OPEN";        // OPEN / IN_PROGRESS / ESCALATED / RESOLVED / CLOSED
    private int currentLevel = 1;
    private User assignedTo;
    private Date createdAt;
    private Date lastStatusChange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ComplaintCategory getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastStatusChange() {
        return lastStatusChange;
    }

    public void setLastStatusChange(Date lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }

    // Computed properties for JSPs
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    public String getAssignedToName() {
        return assignedTo != null ? assignedTo.getName() : null;
    }
}

