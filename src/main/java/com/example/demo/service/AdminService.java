package com.example.demo.service;

import com.example.demo.model.AdminResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired private PoolUserRepository userRepo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private PaymentRepository payRepo;
    @Autowired private SocialImpactRepository impactRepo;
    @Autowired private VehicleRepository vehicleRepo;
    @Autowired private RideParticipationRepository partRepo;
    @Autowired private SosAlertRepository sosRepo;
    @Autowired private RouteScheduleRepository schedRepo;
    @Autowired private NotificationRepository notifRepo;
    @Autowired private RatingRepository ratingRepo;

    public AdminResponse getDashboard() {
        AdminResponse r = new AdminResponse();
        r.setTotalUsers(userRepo.count());
        r.setTotalRides(rideRepo.count());
        r.setTotalPayments(payRepo.count());
        r.setTotalVehicles(vehicleRepo.count());
        r.setTotalParticipations(partRepo.count());
        Double c   = impactRepo.getTotalCarbonSaved(); r.setTotalCarbonSaved(c != null ? c : 0);
        Double rev = payRepo.getTotalRevenue();        r.setTotalRevenue(rev != null ? rev : 0);
        r.setPendingRides(rideRepo.countByStatus("PENDING"));
        r.setActiveRides(rideRepo.countByStatus("ONGOING"));
        r.setCompletedRides(rideRepo.countByStatus("COMPLETED"));
        r.setCancelledRides(rideRepo.countByStatus("CANCELLED"));
        r.setTotalSchedules(schedRepo.count());
        r.setTotalRatings(ratingRepo.count());
        r.setUnreadNotifications(notifRepo.countUnreadAll());
        r.setActiveSos(sosRepo.countByStatus("ACTIVE"));
        return r;
    }

    /** Breakdown of rides by status — used for the admin donut chart */
    public Map<String, Long> getRideStatusBreakdown() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (String s : new String[]{"PENDING","ACCEPTED","ONGOING","COMPLETED","CANCELLED"})
            m.put(s, rideRepo.countByStatus(s));
        return m;
    }

    /** Top eco users for admin leaderboard */
    public List<Map<String, Object>> getTopEcoUsers(int limit) {
        return userRepo.findAll().stream()
            .filter(u -> u.getEcoPoints() != null && u.getEcoPoints() > 0)
            .sorted(Comparator.comparingInt(u -> -u.getEcoPoints()))
            .limit(limit)
            .map(u -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("id", u.getId());
                row.put("name", u.getName());
                row.put("ecoPoints", u.getEcoPoints());
                row.put("totalRides", u.getTotalRides());
                row.put("rating", u.getRating());
                return row;
            })
            .collect(java.util.stream.Collectors.toList());
    }
}
