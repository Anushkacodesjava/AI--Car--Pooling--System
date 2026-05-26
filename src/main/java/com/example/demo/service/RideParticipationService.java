package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class RideParticipationService {
    @Autowired private RideParticipationRepository repo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private PoolUserRepository userRepo;
    @Autowired private NotificationService notifService;

    // @Transactional ensures seat deduction + participation save are atomic —
    // without this two concurrent bookings could both read the same availableSeats
    // and double-book the last seat.
    @Transactional
    public RideParticipation join(Long rideId, Long userId, int seats) {
        RideRequest ride = rideRepo.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Ride #" + rideId + " not found"));

        if ("CANCELLED".equals(ride.getStatus()) || "COMPLETED".equals(ride.getStatus()))
            throw new RuntimeException("This ride is " + ride.getStatus().toLowerCase() + " and cannot be booked");

        int available = ride.getAvailableSeats() != null ? ride.getAvailableSeats()
                      : (ride.getMaxSeats() != null ? ride.getMaxSeats() : 2);

        if (available < seats)
            throw new RuntimeException("Only " + available + " seat(s) available, you requested " + seats);

        PoolUser user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User #" + userId + " not found"));

        // Prevent duplicate booking in the same transaction read
        boolean alreadyBooked = repo.findByRideId(rideId).stream()
            .anyMatch(p -> p.getUser() != null && p.getUser().getId().equals(userId)
                       && !"CANCELLED".equals(p.getStatus()));
        if (alreadyBooked)
            throw new RuntimeException("You have already booked this ride");

        RideParticipation p = new RideParticipation();
        p.setRide(ride);
        p.setUser(user);
        p.setSeatsBooked(seats);
        p.setStatus("CONFIRMED");

        // Deduct seats atomically within this transaction
        ride.setAvailableSeats(available - seats);
        rideRepo.save(ride);

        user.setTotalRides((user.getTotalRides() == null ? 0 : user.getTotalRides()) + 1);
        userRepo.save(user);

        RideParticipation saved = repo.save(p);

        // Notify the driver that someone joined their ride
        if (ride.getUser() != null) {
            try {
                notifService.send(
                    ride.getUser().getId(),
                    "New Booking on Your Ride",
                    user.getName() + " booked " + seats + " seat(s) on your ride from "
                        + ride.getSourceLocation() + " → " + ride.getDestinationLocation(),
                    "RIDE_BOOKED"
                );
            } catch (Exception ignored) { /* don't fail the booking if notification fails */ }
        }

        return saved;
    }

    public List<RideParticipation> getAll() { return repo.findAll(); }
    public List<RideParticipation> getByUser(Long userId) { return repo.findByUserId(userId); }
    public List<RideParticipation> getByRide(Long rideId) { return repo.findByRideId(rideId); }

    @Transactional
    public void cancel(Long id) {
        repo.findById(id).ifPresent(p -> {
            p.setStatus("CANCELLED");
            RideRequest r = p.getRide();
            if (r != null) {
                int cur = r.getAvailableSeats() != null ? r.getAvailableSeats() : 0;
                r.setAvailableSeats(cur + (p.getSeatsBooked() != null ? p.getSeatsBooked() : 1));
                rideRepo.save(r);
            }
            repo.save(p);
        });
    }
}
