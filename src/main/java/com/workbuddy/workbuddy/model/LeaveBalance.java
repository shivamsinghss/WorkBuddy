package com.workbuddy.workbuddy.model;

public class LeaveBalance {
    private String id;
    private String employeeId;
    private String employeeName;
    private int year;
    private int month;
    private int wfhCreditsTotal;   // default 10
    private int wfhUsed;
    private int leaveCreditsTotal; // default 5
    private int leaveUsed;

    public LeaveBalance() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getWfhCreditsTotal() { return wfhCreditsTotal; }
    public void setWfhCreditsTotal(int wfhCreditsTotal) { this.wfhCreditsTotal = wfhCreditsTotal; }
    public int getWfhUsed() { return wfhUsed; }
    public void setWfhUsed(int wfhUsed) { this.wfhUsed = wfhUsed; }
    public int getLeaveCreditsTotal() { return leaveCreditsTotal; }
    public void setLeaveCreditsTotal(int leaveCreditsTotal) { this.leaveCreditsTotal = leaveCreditsTotal; }
    public int getLeaveUsed() { return leaveUsed; }
    public void setLeaveUsed(int leaveUsed) { this.leaveUsed = leaveUsed; }

    public int getWfhRemaining()   { return Math.max(0, wfhCreditsTotal - wfhUsed); }
    public int getLeaveRemaining() { return Math.max(0, leaveCreditsTotal - leaveUsed); }
}
