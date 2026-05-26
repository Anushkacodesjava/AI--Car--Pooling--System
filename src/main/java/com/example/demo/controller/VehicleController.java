package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Vehicle;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/vehicle") @CrossOrigin(origins="*")
public class VehicleController {
    @Autowired private VehicleService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Vehicle>> add(@RequestBody Vehicle v){
        return ResponseEntity.ok(ApiResponse.ok("Vehicle registered successfully", service.add(v)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Vehicle>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<ApiResponse<List<Vehicle>>> byOwner(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(service.getByOwner(id)));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<Vehicle>> verify(@PathVariable Long id){
        Vehicle v = service.verify(id);
        return v != null ? ResponseEntity.ok(ApiResponse.ok("Vehicle verified", v)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle removed", null));
    }
}
