package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.RideParticipation;
import com.example.demo.service.RideParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/participation") @CrossOrigin(origins = "*")
public class RideParticipationController {
    @Autowired private RideParticipationService service;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<RideParticipation>> join(
            @RequestParam Long rideId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int seats) {
        RideParticipation p = service.join(rideId, userId, seats);
        return ResponseEntity.ok(ApiResponse.ok("Booking confirmed! Your Booking ID is #" + p.getId(), p));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RideParticipation>>> all() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RideParticipation>>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByUser(userId)));
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<ApiResponse<List<RideParticipation>>> byRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByRide(rideId)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancel(@PathVariable Long id) {  // was int
        service.cancel(id);
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled. Seat has been restored.", null));
    }
}
