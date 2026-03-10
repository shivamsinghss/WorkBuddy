package com.workbuddy.workbuddy.model;

public class TrainingRecord {
    private String id;
    private String title;
    private String description;
    private String trainer;
    private String startDate;
    private String endDate;
    private String participants; // comma-separated employee IDs
    private String status;       // Upcoming, Ongoing, Completed

    public TrainingRecord() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTrainer() { return trainer; }
    public void setTrainer(String trainer) { this.trainer = trainer; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getParticipants() { return participants; }
    public void setParticipants(String participants) { this.participants = participants; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
