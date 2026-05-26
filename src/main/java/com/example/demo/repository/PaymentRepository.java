package com.example.demo.repository;

import com.example.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByStatus(String status);
    List<Payment> findByMethod(String method);

    // Needed for duplicate-payment guard in PaymentService
    @Query("SELECT p FROM Payment p WHERE p.participation.id = ?1")
    List<Payment> findByParticipationId(int participationId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS'")
    Double getTotalRevenue();
}
