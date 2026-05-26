package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.PoolUser;
import com.example.demo.service.PoolUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/pool") @CrossOrigin(origins="*")
public class PoolUserController {
    @Autowired private PoolUserService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<PoolUser>> add(@Valid @RequestBody PoolUser u){
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", service.addUser(u)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PoolUser>>> all(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="100") int size){
        // Return all for dropdown compatibility, paginate for table view
        if(size>=100) return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
        Page<PoolUser> pg = service.getPaged(PageRequest.of(page,size,Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.ok(pg.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PoolUser>> get(@PathVariable Long id){
        PoolUser u = service.getById(id);
        return u != null ? ResponseEntity.ok(ApiResponse.ok(u)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PoolUser>> update(@PathVariable Long id, @RequestBody PoolUser u){
        PoolUser r = service.update(id, u);
        return r != null ? ResponseEntity.ok(ApiResponse.ok("User updated", r)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully", null));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<Map<String,Object>>> stats(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(service.getUserStats(id)));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<PoolUser>> verify(@PathVariable Long id){
        PoolUser u = service.toggleVerify(id);
        return u != null ? ResponseEntity.ok(ApiResponse.ok("Verification updated", u)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> health(){
        return ResponseEntity.ok(ApiResponse.ok("PoolAI Backend is running", "OK"));
    }
}
