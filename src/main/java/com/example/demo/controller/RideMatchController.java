package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.RideRequest;
import com.example.demo.service.RideMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/ridematch") @CrossOrigin(origins="*")
public class RideMatchController {
    @Autowired private RideMatchService service;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RideRequest>>> search(
            @RequestParam(defaultValue="") String source,
            @RequestParam(defaultValue="") String destination,
            @RequestParam(defaultValue="") String date,
            @RequestParam(defaultValue="") String time,
            @RequestParam(required=false) Integer maxPrice,
            @RequestParam(defaultValue="") String vehicleType,
            @RequestParam(defaultValue="false") Boolean acOnly,
            @RequestParam(defaultValue="false") Boolean ladiesOnly){
        List<RideRequest> results = service.search(source,destination,date,time,maxPrice,vehicleType,acOnly,ladiesOnly);
        String msg = results.isEmpty()
            ? "No rides found matching your criteria"
            : results.size()+" ride(s) found, ranked by AI match score";
        return ResponseEntity.ok(ApiResponse.ok(msg, results));
    }
}
