package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class RatingService {
    @Autowired private RatingRepository repo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private PoolUserRepository userRepo;

    @Transactional
    public Rating give(Long rideId, Long raterId, Long ratedUserId,
                       int score, String comment,
                       Boolean punctual, Boolean clean, Boolean safe) {

        if (score < 1 || score > 5)
            throw new RuntimeException("Rating must be between 1 and 5");

        if (raterId.equals(ratedUserId))
            throw new RuntimeException("You cannot rate yourself");

        // Prevent duplicate rating: one rater → one rated user per ride
        boolean alreadyRated = repo.existsByRideIdAndRaterIdAndRatedUserId(rideId, raterId, ratedUserId);
        if (alreadyRated)
            throw new RuntimeException("You have already rated this user for this ride");

        Rating r = new Rating();
        r.setRide(rideRepo.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found")));
        r.setRater(userRepo.findById(raterId).orElseThrow(() -> new RuntimeException("Rater not found")));
        r.setRatedUser(userRepo.findById(ratedUserId).orElseThrow(() -> new RuntimeException("User not found")));
        r.setRating(score);
        r.setComment(comment);
        r.setPunctual(punctual);
        r.setCleanVehicle(clean);
        r.setSafeDriver(safe);

        Rating saved = repo.save(r);

        // Update user's average rating
        Double avg = repo.avgRatingByUser(ratedUserId);
        if (avg != null) {
            PoolUser u = r.getRatedUser();
            u.setRating(Math.round(avg * 10.0) / 10.0);
            userRepo.save(u);
        }

        return saved;
    }

    public List<Rating> getAll() { return repo.findAll(); }
    public List<Rating> getByUser(Long userId) { return repo.findByRatedUserId(userId); }

    public Map<String, Object> getSummary() {
        List<Rating> all = repo.findAll();
        Map<String, Object> m = new HashMap<>();
        m.put("total", all.size());
        double avg = all.stream().mapToInt(Rating::getRating).average().orElse(0);
        m.put("average", Math.round(avg * 10.0) / 10.0);
        int[] dist = new int[6];
        for (Rating r : all) if (r.getRating() >= 1 && r.getRating() <= 5) dist[r.getRating()]++;
        m.put("distribution", dist);
        long punctual = all.stream().filter(r -> Boolean.TRUE.equals(r.getPunctual())).count();
        long clean    = all.stream().filter(r -> Boolean.TRUE.equals(r.getCleanVehicle())).count();
        long safe     = all.stream().filter(r -> Boolean.TRUE.equals(r.getSafeDriver())).count();
        m.put("punctualPct", all.isEmpty() ? 0 : Math.round((double) punctual / all.size() * 100));
        m.put("cleanPct",    all.isEmpty() ? 0 : Math.round((double) clean    / all.size() * 100));
        m.put("safePct",     all.isEmpty() ? 0 : Math.round((double) safe     / all.size() * 100));
        return m;
    }
}
