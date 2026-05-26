package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.SocialImpact;
import com.example.demo.service.SocialImpactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/impact") @CrossOrigin(origins="*")
public class SocialImpactController {
    @Autowired private SocialImpactService service;

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<SocialImpact>> calculate(@RequestParam Long rideId){
        SocialImpact imp = service.calculate(rideId);
        return ResponseEntity.ok(ApiResponse.ok(
            "Impact calculated! "+imp.getCarbonSaved()+"kg CO₂ saved, +"+imp.getEcoPoints()+" eco points awarded.", imp));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SocialImpact>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/global")
    public ResponseEntity<ApiResponse<Map<String,Object>>> global(){
        return ResponseEntity.ok(ApiResponse.ok(service.getGlobalStats()));
    }
}
