package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "rating")
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne @JoinColumn(name="ride_id") private RideRequest ride;
    @ManyToOne @JoinColumn(name="rater_id") private PoolUser rater;
    @ManyToOne @JoinColumn(name="rated_user_id") private PoolUser ratedUser;
    private int rating; // 1-5
    private String comment;
    private Boolean punctual;
    private Boolean cleanVehicle;
    private Boolean safeDriver;
    private LocalDateTime createdAt;
    @PrePersist void onCreate(){ createdAt=LocalDateTime.now(); }

    public int getId(){return id;} public void setId(int id){this.id=id;}
    public RideRequest getRide(){return ride;} public void setRide(RideRequest r){this.ride=r;}
    public PoolUser getRater(){return rater;} public void setRater(PoolUser r){this.rater=r;}
    public PoolUser getRatedUser(){return ratedUser;} public void setRatedUser(PoolUser r){this.ratedUser=r;}
    public int getRating(){return rating;} public void setRating(int r){this.rating=r;}
    public String getComment(){return comment;} public void setComment(String c){this.comment=c;}
    public Boolean getPunctual(){return punctual;} public void setPunctual(Boolean p){this.punctual=p;}
    public Boolean getCleanVehicle(){return cleanVehicle;} public void setCleanVehicle(Boolean c){this.cleanVehicle=c;}
    public Boolean getSafeDriver(){return safeDriver;} public void setSafeDriver(Boolean s){this.safeDriver=s;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}
