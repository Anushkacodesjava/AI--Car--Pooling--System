package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/payment") @CrossOrigin(origins="*")
public class PaymentController {
    @Autowired private PaymentService service;

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<Payment>> pay(
            @RequestParam int participationId,
            @RequestParam double amount,
            @RequestParam String method){
        if(amount<=0) throw new RuntimeException("Payment amount must be greater than zero");
        Payment p = service.pay(participationId, amount, method);
        return ResponseEntity.ok(ApiResponse.ok(
            "Payment of ₹"+amount+" recorded. Transaction ID: "+p.getTransactionId(), p));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Payment>>> all(){
        return ResponseEntity.ok(ApiResponse.ok(service.getAll()));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String,Object>>> summary(){
        return ResponseEntity.ok(ApiResponse.ok(service.getSummary()));
    }
}
