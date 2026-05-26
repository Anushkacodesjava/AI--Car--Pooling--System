package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="route_schedules")
public class RouteSchedule {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id") private PoolUser user;
    private String sourceLocation;
    private String destinationLocation;
    private String departureTime; // HH:mm
    private String daysOfWeek;   // e.g. "MON,TUE,WED,THU,FRI"
    private Integer seats;
    private Double pricePerSeat;
    private String vehicleType;
    private Boolean isActive = true;
    private Integer totalRidesGenerated = 0;
    private LocalDateTime createdAt;
    @PrePersist void onCreate(){ createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public PoolUser getUser(){return user;} public void setUser(PoolUser u){this.user=u;}
    public String getSourceLocation(){return sourceLocation;} public void setSourceLocation(String s){this.sourceLocation=s;}
    public String getDestinationLocation(){return destinationLocation;} public void setDestinationLocation(String d){this.destinationLocation=d;}
    public String getDepartureTime(){return departureTime;} public void setDepartureTime(String t){this.departureTime=t;}
    public String getDaysOfWeek(){return daysOfWeek;} public void setDaysOfWeek(String d){this.daysOfWeek=d;}
    public Integer getSeats(){return seats;} public void setSeats(Integer s){this.seats=s;}
    public Double getPricePerSeat(){return pricePerSeat;} public void setPricePerSeat(Double p){this.pricePerSeat=p;}
    public String getVehicleType(){return vehicleType;} public void setVehicleType(String v){this.vehicleType=v;}
    public Boolean getIsActive(){return isActive;} public void setIsActive(Boolean a){this.isActive=a;}
    public Integer getTotalRidesGenerated(){return totalRidesGenerated;} public void setTotalRidesGenerated(Integer t){this.totalRidesGenerated=t;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}
