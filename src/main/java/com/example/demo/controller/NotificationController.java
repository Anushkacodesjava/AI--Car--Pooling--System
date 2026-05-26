package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/notification") @CrossOrigin(origins="*")
public class NotificationController {
    @Autowired private NotificationService service;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Notification>> send(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(defaultValue="SYSTEM") String type){
        return ResponseEntity.ok(ApiResponse.ok("Notification sent", service.send(userId, title, message, type)));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<ApiResponse<String>> broadcast(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(defaultValue="SYSTEM") String type){
        service.broadcast(title, message, type);
        return ResponseEntity.ok(ApiResponse.ok("Broadcast sent to all users", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Notification>>> byUser(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.ok(service.getByUser(userId)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Notification>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<ApiResponse<Map<String,Long>>> unread(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.ok(Map.of("count", service.countUnread(userId))));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Notification>> markRead(@PathVariable Long id){
        Notification n = service.markRead(id);
        return n != null ? ResponseEntity.ok(ApiResponse.ok("Marked as read", n)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/readAll/{userId}")
    public ResponseEntity<ApiResponse<String>> markAllRead(@PathVariable Long userId){
        service.markAllRead(userId);
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read", null));
    }
}
