package com.example.demo.repository;

import com.example.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByRatedUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.ratedUser.id = ?1")
    Double avgRatingByUser(Long userId);

    @Query("SELECT AVG(r.rating) FROM Rating r")
    Double overallAvgRating();

    // Needed for duplicate-rating guard
    boolean existsByRideIdAndRaterIdAndRatedUserId(Long rideId, Long raterId, Long ratedUserId);
}
