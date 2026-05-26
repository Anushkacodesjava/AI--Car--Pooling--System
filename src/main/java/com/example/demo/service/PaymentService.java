package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class PaymentService {
    @Autowired private PaymentRepository repo;
    @Autowired private RideParticipationRepository partRepo;

    @Transactional
    public Payment pay(int participationId, double amount, String method) {
        RideParticipation p = partRepo.findById((long)(participationId))
            .orElseThrow(() -> new RuntimeException("Booking #" + participationId + " not found"));

        if ("CANCELLED".equals(p.getStatus()))
            throw new RuntimeException("Cannot pay for a cancelled booking");

        // Guard against duplicate payments for the same booking
        boolean alreadyPaid = repo.findByParticipationId(participationId).stream()
            .anyMatch(pay -> "SUCCESS".equals(pay.getStatus()));
        if (alreadyPaid)
            throw new RuntimeException("Booking #" + participationId + " has already been paid");

        if (amount <= 0)
            throw new RuntimeException("Payment amount must be greater than zero");

        Payment pay = new Payment();
        pay.setParticipation(p);
        pay.setAmount(amount);
        pay.setMethod(method);
        pay.setStatus("SUCCESS");
        return repo.save(pay);
    }

    public List<Payment> getAll() { return repo.findAll(); }

    public Map<String, Object> getSummary() {
        Map<String, Object> m = new HashMap<>();
        List<Payment> all = repo.findAll();
        m.put("total", all.size());
        m.put("totalRevenue", all.stream()
            .filter(p -> "SUCCESS".equals(p.getStatus()))
            .mapToDouble(Payment::getAmount).sum());
        Map<String, Long> byMethod = new HashMap<>();
        for (Payment p : all) byMethod.merge(p.getMethod(), 1L, Long::sum);
        m.put("byMethod", byMethod);
        return m;
    }
}
