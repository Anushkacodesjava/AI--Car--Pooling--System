package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="*")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req){
        AuthResponse res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Account created successfully! Welcome to PoolAI.", res));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req){
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok("Login successful. Welcome back, "+res.getName()+"!", res));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(@Valid @RequestBody AuthRequest req){
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok("Admin login successful.", res));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> check(){
        return ResponseEntity.ok(ApiResponse.ok("Token is valid", "Authenticated"));
    }
}
