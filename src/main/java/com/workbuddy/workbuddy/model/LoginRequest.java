package com.workbuddy.workbuddy.model;

public class LoginRequest {
    private String username; // can be username or email
    private String password;

    public LoginRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
