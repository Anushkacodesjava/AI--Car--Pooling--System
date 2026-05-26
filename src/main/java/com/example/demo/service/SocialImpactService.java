package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class SocialImpactService {
    @Autowired private SocialImpactRepository repo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private PoolUserRepository userRepo;
    @Autowired private RideParticipationRepository partRepo;

    @Transactional
    public SocialImpact calculate(Long rideId) {
        RideRequest ride = rideRepo.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Ride #" + rideId + " not found"));

        long riders = partRepo.countByRideId(rideId);
        double distKm = ride.getEstimatedDistanceKm() != null ? ride.getEstimatedDistanceKm() : 15.0;
        double carsTakenOff = Math.max(riders - 1, 1);
        double fuelSaved    = carsTakenOff * distKm * 0.07;  // 7L/100km
        double carbonSaved  = fuelSaved * 2.31;               // kg CO₂ per litre petrol
        double moneySaved   = carsTakenOff * distKm * 8;     // ~₹8/km

        // Total eco points for the ride
        int totalEcoPoints = (int) (carbonSaved * 4 + fuelSaved * 3);

        // Driver earns 60% of points (they made the trip possible),
        // each passenger splits the remaining 40%
        int driverPoints    = (int) (totalEcoPoints * 0.60);
        int passengerPoints = riders > 0 ? (int) (totalEcoPoints * 0.40 / riders) : 0;

        // Award driver
        if (ride.getUser() != null) {
            PoolUser driver = ride.getUser();
            driver.setEcoPoints((driver.getEcoPoints() == null ? 0 : driver.getEcoPoints()) + driverPoints);
            driver.setTotalRidesOffered((driver.getTotalRidesOffered() == null ? 0 : driver.getTotalRidesOffered()) + 1);
            userRepo.save(driver);
        }

        // Award each passenger
        partRepo.findByRideId(rideId).forEach(p -> {
            PoolUser u = p.getUser();
            if (u != null) {
                u.setEcoPoints((u.getEcoPoints() == null ? 0 : u.getEcoPoints()) + passengerPoints);
                userRepo.save(u);
            }
        });

        SocialImpact impact = new SocialImpact();
        impact.setRide(ride);
        impact.setDistanceKm(distKm);
        impact.setFuelSaved(Math.round(fuelSaved  * 100.0) / 100.0);
        impact.setCarbonSaved(Math.round(carbonSaved * 100.0) / 100.0);
        impact.setMoneySaved(Math.round(moneySaved * 100.0) / 100.0);
        impact.setEcoPoints(totalEcoPoints);
        return repo.save(impact);
    }

    public List<SocialImpact> getAll() { return repo.findAll(); }

    public Map<String, Object> getGlobalStats() {
        Map<String, Object> m = new HashMap<>();
        Double c  = repo.getTotalCarbonSaved();  m.put("totalCarbon",    c  != null ? Math.round(c  * 10.0) / 10.0 : 0);
        Double f  = repo.getTotalFuelSaved();     m.put("totalFuel",      f  != null ? Math.round(f  * 10.0) / 10.0 : 0);
        Long   e  = repo.getTotalEcoPoints();     m.put("totalEcoPoints", e  != null ? e : 0);
        Double ms = repo.getTotalMoneySaved();    m.put("totalMoneySaved",ms != null ? Math.round(ms) : 0);
        double carbon = c != null ? c : 0;
        m.put("treesEquivalent",  (int) (carbon / 21.7));
        m.put("kmDrivenAvoided",  (int) (carbon / 0.12));
        return m;
    }
}
