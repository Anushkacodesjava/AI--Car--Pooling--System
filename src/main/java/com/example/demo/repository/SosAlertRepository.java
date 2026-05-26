package com.example.demo.repository;

import com.example.demo.model.SosAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SosAlertRepository extends JpaRepository<SosAlert, Long> {
    List<SosAlert> findByStatus(String status);
    // Fixed: was findByOrderByCreatedAtDesc (missing 'All') — caused NoSuchMethodError
    List<SosAlert> findAllByOrderByCreatedAtDesc();
    long countByStatus(String status);
}
