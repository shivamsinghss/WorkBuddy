package com.workbuddy.workbuddy.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private String id;
    private String name;
    private String email;
    private String role;
    private String avatar;
    private String department;
    private String position;

    public LoginResponse() {}

    public static LoginResponse ok(String id, String name, String email, String role,
                                   String avatar, String department, String position) {
        LoginResponse r = new LoginResponse();
        r.success = true;
        r.message = "Login successful";
        r.id = id;
        r.name = name;
        r.email = email;
        r.role = role;
        r.avatar = avatar;
        r.department = department;
        r.position = position;
        return r;
    }

    public static LoginResponse fail(String message) {
        LoginResponse r = new LoginResponse();
        r.success = false;
        r.message = message;
        return r;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getAvatar() { return avatar; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
}
