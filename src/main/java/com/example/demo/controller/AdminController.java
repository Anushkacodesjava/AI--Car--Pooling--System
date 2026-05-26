package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.AdminResponse;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController @RequestMapping("/admin") @CrossOrigin(origins = "*")
public class AdminController {
    @Autowired private AdminService service;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.ok(service.getDashboard()));
    }

    @GetMapping("/ride-status")
    public ResponseEntity<ApiResponse<Map<String, Long>>> rideStatus() {
        return ResponseEntity.ok(ApiResponse.ok(service.getRideStatusBreakdown()));
    }

    @GetMapping("/top-eco")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> topEco(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.ok(service.getTopEcoUsers(limit)));
    }
}
