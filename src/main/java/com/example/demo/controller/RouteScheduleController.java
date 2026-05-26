package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.*;
import com.example.demo.service.RouteScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/schedule") @CrossOrigin(origins="*")
public class RouteScheduleController {
    @Autowired private RouteScheduleService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RouteSchedule>> add(@RequestBody RouteSchedule s){
        if(s.getSourceLocation()==null||s.getSourceLocation().isBlank())
            throw new RuntimeException("Source location is required");
        if(s.getDestinationLocation()==null||s.getDestinationLocation().isBlank())
            throw new RuntimeException("Destination location is required");
        return ResponseEntity.ok(ApiResponse.ok("Schedule created successfully", service.save(s)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RouteSchedule>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RouteSchedule>>> byUser(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.ok(service.getByUser(userId)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RouteSchedule>>> active(){
        return ResponseEntity.ok(ApiResponse.ok(service.getActive()));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<RouteSchedule>> toggle(@PathVariable Long id){
        RouteSchedule s = service.toggle(id);
        return s != null ? ResponseEntity.ok(ApiResponse.ok("Schedule "+(s.getIsActive()?"activated":"paused"), s)) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/generate")
    public ResponseEntity<ApiResponse<RideRequest>> generate(@PathVariable Long id){
        RideRequest r = service.generateRide(id);
        return ResponseEntity.ok(ApiResponse.ok("Ride #"+r.getId()+" generated for today!", r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Schedule deleted", null));
    }
}
