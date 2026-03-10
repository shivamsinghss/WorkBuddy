package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.LoginRequest;
import com.workbuddy.workbuddy.model.LoginResponse;
import com.workbuddy.workbuddy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) throws IOException {
        LoginResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }
}
