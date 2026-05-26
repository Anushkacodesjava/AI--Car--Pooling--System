package com.example.demo.service;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RouteScheduleService {
    @Autowired private RouteScheduleRepository repo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private PoolUserRepository userRepo;

    public RouteSchedule save(RouteSchedule s){ return repo.save(s); }
    public List<RouteSchedule> getByUser(Long userId){ return repo.findByUserId(userId); }
    public List<RouteSchedule> getActive(){ return repo.findByIsActiveTrue(); }
    public List<RouteSchedule> getAll(){ return repo.findAll(); }
    public void delete(Long id){ repo.deleteById(id); }
    public RouteSchedule toggle(Long id){ return repo.findById(id).map(s->{ s.setIsActive(!Boolean.TRUE.equals(s.getIsActive())); return repo.save(s); }).orElse(null); }

    // Generate today's ride from a schedule
    public RideRequest generateRide(Long scheduleId){
        RouteSchedule s = repo.findById(scheduleId).orElseThrow();
        RideRequest r = new RideRequest();
        r.setUser(s.getUser());
        r.setSourceLocation(s.getSourceLocation());
        r.setDestinationLocation(s.getDestinationLocation());
        r.setRideDate(LocalDate.now().toString());
        r.setRideTime(s.getDepartureTime());
        r.setMaxSeats(s.getSeats()); r.setAvailableSeats(s.getSeats());
        r.setPricePerSeat(s.getPricePerSeat());
        r.setVehicleType(s.getVehicleType());
        r.setStatus("PENDING");
        r.setNotes("Auto-generated from schedule");
        RideRequest saved = rideRepo.save(r);
        s.setTotalRidesGenerated(s.getTotalRidesGenerated()+1);
        repo.save(s);
        return saved;
    }
}
