package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.RideRequest;
import com.example.demo.service.RideRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/ride") @CrossOrigin(origins="*")
public class RideRequestController {
    @Autowired private RideRequestService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RideRequest>> add(@Valid @RequestBody RideRequest r){
        return ResponseEntity.ok(ApiResponse.ok("Ride posted successfully", service.save(r)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RideRequest>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RideRequest>>> active(){
        return ResponseEntity.ok(ApiResponse.ok(service.getActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RideRequest>> get(@PathVariable Long id){
        RideRequest r = service.getById(id);
        return r != null ? ResponseEntity.ok(ApiResponse.ok(r)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RideRequest>> update(@PathVariable Long id, @RequestBody RideRequest r){
        RideRequest u = service.update(id, r);
        return u != null ? ResponseEntity.ok(ApiResponse.ok("Ride updated", u)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RideRequest>> status(@PathVariable Long id, @RequestBody Map<String,String> body){
        String st = body.get("status");
        if(st==null) throw new RuntimeException("Status field is required");
        RideRequest r = service.updateStatus(id, st);
        return r != null ? ResponseEntity.ok(ApiResponse.ok("Status updated to "+st, r)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Ride deleted", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RideRequest>>> byUser(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.ok(service.getByUser(userId)));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String,Long>>> stats(){
        return ResponseEntity.ok(ApiResponse.ok(service.getStatusCounts()));
    }
}
