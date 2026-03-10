package com.workbuddy.workbuddy.model;

public class AttendanceRecord {
    private String id;
    private String employeeId;
    private String employeeName;
    private String date;        // YYYY-MM-DD
    private String type;        // Present | WFH | Absent
    private String punchIn;     // HH:mm
    private String punchOut;    // HH:mm
    private int month;
    private int year;

    public AttendanceRecord() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPunchIn() { return punchIn; }
    public void setPunchIn(String punchIn) { this.punchIn = punchIn; }
    public String getPunchOut() { return punchOut; }
    public void setPunchOut(String punchOut) { this.punchOut = punchOut; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
