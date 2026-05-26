package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.SosAlert;
import com.example.demo.service.SosAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/sos") @CrossOrigin(origins="*")
public class SosAlertController {
    @Autowired private SosAlertService service;

    @PostMapping("/raise")
    public ResponseEntity<ApiResponse<SosAlert>> raise(
            @RequestParam Long userId,
            @RequestParam(required=false) Long rideId,
            @RequestParam(defaultValue="Unknown location") String location,
            @RequestParam(required=false) Double lat,
            @RequestParam(required=false) Double lng){
        SosAlert s = service.raise(userId, rideId, location, lat, lng);
        return ResponseEntity.ok(ApiResponse.ok("SOS Alert raised! Help is being notified.", s));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SosAlert>>> active(){
        return ResponseEntity.ok(ApiResponse.ok(service.getActive()));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SosAlert>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<SosAlert>> resolve(
            @PathVariable Long id,
            @RequestParam(defaultValue="Admin") String resolvedBy){
        SosAlert s = service.resolve(id, resolvedBy);
        return s != null ? ResponseEntity.ok(ApiResponse.ok("Alert resolved by "+resolvedBy, s)) : ResponseEntity.notFound().build();
    }
}
