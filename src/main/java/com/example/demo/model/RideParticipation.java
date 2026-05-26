package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_participation",
    indexes = {
        @Index(name = "idx_part_user", columnList = "user_id"),
        @Index(name = "idx_part_ride", columnList = "ride_id")
    })
public class RideParticipation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // was int — changed to Long for consistency

    @ManyToOne @JoinColumn(name = "user_id") private PoolUser user;
    @ManyToOne @JoinColumn(name = "ride_id") private RideRequest ride;

    private String status;       // CONFIRMED, CANCELLED
    private Integer seatsBooked = 1;
    private LocalDateTime bookedAt;

    @PrePersist void onCreate() { bookedAt = LocalDateTime.now(); }

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }
    public PoolUser getUser()              { return user; }
    public void setUser(PoolUser u)        { this.user = u; }
    public RideRequest getRide()           { return ride; }
    public void setRide(RideRequest r)     { this.ride = r; }
    public String getStatus()              { return status; }
    public void setStatus(String s)        { this.status = s; }
    public Integer getSeatsBooked()        { return seatsBooked; }
    public void setSeatsBooked(Integer s)  { this.seatsBooked = s; }
    public LocalDateTime getBookedAt()     { return bookedAt; }
    public void setBookedAt(LocalDateTime b){ this.bookedAt = b; }
}
