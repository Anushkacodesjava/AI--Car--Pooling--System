package com.example.demo.repository;

import com.example.demo.model.RideParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RideParticipationRepository extends JpaRepository<RideParticipation, Long> {
    List<RideParticipation> findByUserId(Long userId);
    List<RideParticipation> findByRideId(Long rideId);
    List<RideParticipation> findByStatus(String status);
    long countByRideId(Long rideId);
}
