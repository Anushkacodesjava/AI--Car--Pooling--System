package com.example.demo.service;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SosAlertService {
    @Autowired private SosAlertRepository repo;
    @Autowired private PoolUserRepository userRepo;
    @Autowired private RideRequestRepository rideRepo;
    @Autowired private NotificationService notifService;

    public SosAlert raise(Long userId, Long rideId, String location, Double lat, Double lng){
        SosAlert s = new SosAlert();
        s.setUser(userRepo.findById(userId).orElseThrow());
        if(rideId!=null) s.setRide(rideRepo.findById(rideId).orElse(null));
        s.setLocationDescription(location); s.setLatitude(lat); s.setLongitude(lng);
        SosAlert saved = repo.save(s);
        // Notify all admins/system
        notifService.broadcast("🚨 SOS ALERT", "User "+s.getUser().getName()+" raised an SOS at: "+location, "SOS");
        return saved;
    }
    public List<SosAlert> getActive(){ return repo.findByStatus("ACTIVE"); }
    public List<SosAlert> getAll(){ return repo.findAllByOrderByCreatedAtDesc(); }
    public SosAlert resolve(Long id, String resolvedBy){
        return repo.findById(id).map(s->{ s.setStatus("RESOLVED"); s.setResolvedBy(resolvedBy); s.setResolvedAt(LocalDateTime.now()); return repo.save(s); }).orElse(null);
    }
}
