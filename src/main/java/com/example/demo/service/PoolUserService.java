package com.example.demo.service;

import com.example.demo.model.PoolUser;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PoolUserService {
    @Autowired private PoolUserRepository repo;
    @Autowired private RatingRepository ratingRepo;
    @Autowired(required=false) private PasswordEncoder passwordEncoder;

    public PoolUser addUser(PoolUser u){
        if(u.getIsActive()==null) u.setIsActive(true);
        if(u.getTotalRides()==null) u.setTotalRides(0);
        if(u.getRating()==null) u.setRating(5.0);
        if(u.getEcoPoints()==null) u.setEcoPoints(0);
        // Hash password if provided and encoder available
        if(u.getPassword()!=null && !u.getPassword().isEmpty() && passwordEncoder!=null)
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public List<PoolUser> getAll(){ return repo.findAll(); }

    public Page<PoolUser> getPaged(Pageable pageable){ return repo.findAll(pageable); }

    public PoolUser getById(Long id){ return repo.findById(id).orElse(null); }

    public PoolUser update(Long id, PoolUser u){
        return repo.findById(id).map(e->{
            if(u.getName()!=null) e.setName(u.getName());
            if(u.getEmail()!=null) e.setEmail(u.getEmail());
            if(u.getPhone()!=null) e.setPhone(u.getPhone());
            if(u.getRole()!=null) e.setRole(u.getRole());
            if(u.getHomeLocation()!=null) e.setHomeLocation(u.getHomeLocation());
            if(u.getOfficeLocation()!=null) e.setOfficeLocation(u.getOfficeLocation());
            if(u.getGender()!=null) e.setGender(u.getGender());
            if(u.getIsActive()!=null) e.setIsActive(u.getIsActive());
            if(u.getPreferredPayment()!=null) e.setPreferredPayment(u.getPreferredPayment());
            return repo.save(e);
        }).orElse(null);
    }

    public void delete(Long id){ repo.deleteById(id); }

    public Map<String,Object> getUserStats(Long id){
        PoolUser u = repo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        Map<String,Object> stats = new HashMap<>();
        stats.put("user", u);
        Double avg = ratingRepo.avgRatingByUser(id);
        stats.put("avgRating", avg!=null ? Math.round(avg*10.0)/10.0 : 5.0);
        return stats;
    }

    public PoolUser toggleVerify(Long id){
        return repo.findById(id).map(u->{
            u.setIsVerified(!Boolean.TRUE.equals(u.getIsVerified()));
            return repo.save(u);
        }).orElse(null);
    }
}
