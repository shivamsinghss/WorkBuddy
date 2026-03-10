package com.workbuddy.workbuddy.model;

public class RecruitmentPost {
    private String id;
    private String title;
    private String department;
    private String description;
    private String location;
    private String type;         // Full-time, Part-time, Contract, Internship
    private String status;       // Open, On-Hold, Closed
    private String postedDate;
    private String closingDate;
    private int applicants;

    public RecruitmentPost() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }
    public String getClosingDate() { return closingDate; }
    public void setClosingDate(String closingDate) { this.closingDate = closingDate; }
    public int getApplicants() { return applicants; }
    public void setApplicants(int applicants) { this.applicants = applicants; }
}
