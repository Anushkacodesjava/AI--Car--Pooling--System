package com.example.demo.service;
import com.example.demo.model.Vehicle;
import com.example.demo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VehicleService {
    @Autowired private VehicleRepository repo;
    public Vehicle add(Vehicle v){ return repo.save(v); }
    public List<Vehicle> getAll(){ return repo.findAll(); }
    public List<Vehicle> getByOwner(Long id){ return repo.findByOwnerId(id); }
    public Vehicle verify(Long id){ return repo.findById(id).map(v->{ v.setIsVerified(true); return repo.save(v); }).orElse(null); }
    public void delete(Long id){ repo.deleteById(id); }
}
