package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="sos_alerts")
public class SosAlert {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id") private PoolUser user;
    @ManyToOne @JoinColumn(name="ride_id") private RideRequest ride;
    private String locationDescription;
    private Double latitude;
    private Double longitude;
    private String status; // ACTIVE, RESOLVED, DISMISSED
    private String resolvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    @PrePersist void onCreate(){ createdAt=LocalDateTime.now(); status="ACTIVE"; }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public PoolUser getUser(){return user;} public void setUser(PoolUser u){this.user=u;}
    public RideRequest getRide(){return ride;} public void setRide(RideRequest r){this.ride=r;}
    public String getLocationDescription(){return locationDescription;} public void setLocationDescription(String l){this.locationDescription=l;}
    public Double getLatitude(){return latitude;} public void setLatitude(Double l){this.latitude=l;}
    public Double getLongitude(){return longitude;} public void setLongitude(Double l){this.longitude=l;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public String getResolvedBy(){return resolvedBy;} public void setResolvedBy(String r){this.resolvedBy=r;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
    public LocalDateTime getResolvedAt(){return resolvedAt;} public void setResolvedAt(LocalDateTime r){this.resolvedAt=r;}
}
