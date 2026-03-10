package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.Employee;
import com.workbuddy.workbuddy.model.LoginRequest;
import com.workbuddy.workbuddy.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuthService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    @Autowired
    private EmployeeService employeeService;

    public LoginResponse login(LoginRequest req) throws IOException {
        String username = req.getUsername() == null ? "" : req.getUsername().trim();
        String password = req.getPassword() == null ? "" : req.getPassword().trim();

        if (username.isEmpty() || password.isEmpty()) {
            return LoginResponse.fail("Username and password are required");
        }

        // Check admin credentials
        if (username.equalsIgnoreCase(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return LoginResponse.ok("admin", "Admin", "admin@workbuddy.com", "admin",
                    "AD", "Administration", "Administrator");
        }

        // Check employees Excel
        List<Employee> employees = employeeService.getAll();
        for (Employee emp : employees) {
            boolean usernameMatch = username.equalsIgnoreCase(emp.getUsername())
                    || username.equalsIgnoreCase(emp.getEmail());
            boolean passwordMatch = password.equals(emp.getPassword());
            if (usernameMatch && passwordMatch) {
                String name = emp.getFirstName() + " " + emp.getLastName();
                String avatar = initials(name);
                return LoginResponse.ok(emp.getId(), name, emp.getEmail(),
                        "employee", avatar, emp.getDepartment(), emp.getPosition());
            }
        }

        return LoginResponse.fail("Invalid username or password");
    }

    private String initials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) if (!p.isEmpty()) sb.append(Character.toUpperCase(p.charAt(0)));
        return sb.length() > 2 ? sb.substring(0, 2) : sb.toString();
    }
}
