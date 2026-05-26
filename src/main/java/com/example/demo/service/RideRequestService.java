package com.example.demo.service;

import com.example.demo.model.RideRequest;
import com.example.demo.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RideRequestService {
    @Autowired private RideRequestRepository repo;

    public RideRequest save(RideRequest r){
        if(r.getStatus()==null) r.setStatus("PENDING");
        if(r.getAvailableSeats()==null && r.getMaxSeats()!=null)
            r.setAvailableSeats(r.getMaxSeats());
        if(r.getAvailableSeats()==null) r.setAvailableSeats(2); // safe default
        if(r.getViewCount()==null) r.setViewCount(0);
        return repo.save(r);
    }

    public List<RideRequest> getAll(){ return repo.findAll(); }
    public List<RideRequest> getActive(){ return repo.findAllActiveRides(); }

    public RideRequest getById(Long id){
        return repo.findById(id).map(r->{
            r.setViewCount((r.getViewCount()==null?0:r.getViewCount())+1);
            return repo.save(r);
        }).orElse(null);
    }

    public RideRequest updateStatus(Long id, String status){
        return repo.findById(id).map(r->{ r.setStatus(status); return repo.save(r); }).orElse(null);
    }

    public RideRequest update(Long id, RideRequest u){
        return repo.findById(id).map(r->{
            if(u.getSourceLocation()!=null) r.setSourceLocation(u.getSourceLocation());
            if(u.getDestinationLocation()!=null) r.setDestinationLocation(u.getDestinationLocation());
            if(u.getRideDate()!=null) r.setRideDate(u.getRideDate());
            if(u.getRideTime()!=null) r.setRideTime(u.getRideTime());
            if(u.getStatus()!=null) r.setStatus(u.getStatus());
            if(u.getAvailableSeats()!=null) r.setAvailableSeats(u.getAvailableSeats());
            if(u.getPricePerSeat()!=null) r.setPricePerSeat(u.getPricePerSeat());
            if(u.getNotes()!=null) r.setNotes(u.getNotes());
            if(u.getVehicleType()!=null) r.setVehicleType(u.getVehicleType());
            if(u.getAcAvailable()!=null) r.setAcAvailable(u.getAcAvailable());
            if(u.getLadiesOnly()!=null) r.setLadiesOnly(u.getLadiesOnly());
            return repo.save(r);
        }).orElse(null);
    }

    public void delete(Long id){ repo.deleteById(id); }
    public List<RideRequest> getByUser(Long userId){ return repo.findByUserId(userId); }

    public Map<String,Long> getStatusCounts(){
        Map<String,Long> m = new HashMap<>();
        m.put("PENDING", repo.countByStatus("PENDING"));
        m.put("ACCEPTED", repo.countByStatus("ACCEPTED"));
        m.put("ONGOING", repo.countByStatus("ONGOING"));
        m.put("COMPLETED", repo.countByStatus("COMPLETED"));
        m.put("CANCELLED", repo.countByStatus("CANCELLED"));
        return m;
    }
}
