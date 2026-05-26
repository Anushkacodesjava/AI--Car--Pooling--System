package com.example.demo.model;
import jakarta.persistence.*;

@Entity @Table(name = "social_impact")
public class SocialImpact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double carbonSaved;
    private double fuelSaved;
    private int ecoPoints;
    private double moneySaved;
    private double distanceKm;
    @OneToOne @JoinColumn(name="ride_id") private RideRequest ride;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public double getCarbonSaved(){return carbonSaved;} public void setCarbonSaved(double c){this.carbonSaved=c;}
    public double getFuelSaved(){return fuelSaved;} public void setFuelSaved(double f){this.fuelSaved=f;}
    public int getEcoPoints(){return ecoPoints;} public void setEcoPoints(int e){this.ecoPoints=e;}
    public double getMoneySaved(){return moneySaved;} public void setMoneySaved(double m){this.moneySaved=m;}
    public double getDistanceKm(){return distanceKm;} public void setDistanceKm(double d){this.distanceKm=d;}
    public RideRequest getRide(){return ride;} public void setRide(RideRequest r){this.ride=r;}
}
