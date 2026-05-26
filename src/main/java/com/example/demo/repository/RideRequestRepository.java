package com.example.demo.repository;
import com.example.demo.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    List<RideRequest> findByStatus(String status);
    List<RideRequest> findByUserId(Long userId);
    List<RideRequest> findByAvailableSeatsGreaterThan(int seats);
    @Query("SELECT r FROM RideRequest r WHERE r.status NOT IN ('COMPLETED','CANCELLED') AND r.availableSeats > 0 ORDER BY r.createdAt DESC")
    List<RideRequest> findAllActiveRides();
    @Query("SELECT COUNT(r) FROM RideRequest r WHERE r.status = ?1") long countByStatus(String status);
}
