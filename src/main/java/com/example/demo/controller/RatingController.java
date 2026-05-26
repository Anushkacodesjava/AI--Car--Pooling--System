package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Rating;
import com.example.demo.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/rating") @CrossOrigin(origins="*")
public class RatingController {
    @Autowired private RatingService service;

    @PostMapping("/give")
    public ResponseEntity<ApiResponse<Rating>> give(
            @RequestParam Long rideId,
            @RequestParam Long raterId,
            @RequestParam Long ratedUserId,
            @RequestParam int score,
            @RequestParam(defaultValue="") String comment,
            @RequestParam(defaultValue="false") Boolean punctual,
            @RequestParam(defaultValue="false") Boolean cleanVehicle,
            @RequestParam(defaultValue="false") Boolean safeDriver){
        if(score<1||score>5) throw new RuntimeException("Score must be between 1 and 5");
        if(raterId.equals(ratedUserId)) throw new RuntimeException("You cannot rate yourself");
        Rating r = service.give(rideId, raterId, ratedUserId, score, comment, punctual, cleanVehicle, safeDriver);
        return ResponseEntity.ok(ApiResponse.ok("Rating submitted! Thank you for your feedback.", r));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Rating>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Rating>>> byUser(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.ok(service.getByUser(userId)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String,Object>>> summary(){
        return ResponseEntity.ok(ApiResponse.ok(service.getSummary()));
    }
}
