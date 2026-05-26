package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity @Table(name="ride_request",
    indexes={
        @Index(name="idx_ride_status", columnList="status"),
        @Index(name="idx_ride_date", columnList="rideDate"),
        @Index(name="idx_ride_user", columnList="user_id")
    })
public class RideRequest {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;

    @NotBlank(message="Source location is required")
    private String sourceLocation;

    @NotBlank(message="Destination location is required")
    private String destinationLocation;

    private String rideDate;
    private String rideTime;

    @Pattern(regexp="PENDING|ACCEPTED|ONGOING|COMPLETED|CANCELLED",
             message="Status must be PENDING, ACCEPTED, ONGOING, COMPLETED or CANCELLED")
    private String status = "PENDING";

    @Min(value=1, message="Must have at least 1 seat")
    @Max(value=8, message="Cannot offer more than 8 seats")
    private Integer maxSeats;

    private Integer availableSeats;

    @DecimalMin(value="0.0", message="Price cannot be negative")
    private Double pricePerSeat;

    @Size(max=500, message="Notes cannot exceed 500 characters")
    private String notes;

    private String vehicleType;
    private Boolean acAvailable = false;
    private Boolean ladiesOnly = false;
    private Double estimatedDistanceKm;
    private Integer estimatedDurationMin;
    private Integer viewCount = 0;
    private LocalDateTime createdAt;

    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id")
    private PoolUser user;

    @Transient private int aiMatchScore;
    @Transient private String aiMatchLabel;

    @PrePersist void onCreate(){
        createdAt = LocalDateTime.now();
        if(status==null) status="PENDING";
        if(availableSeats==null && maxSeats!=null) availableSeats=maxSeats;
        if(viewCount==null) viewCount=0;
    }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSourceLocation(){return sourceLocation;} public void setSourceLocation(String s){this.sourceLocation=s;}
    public String getDestinationLocation(){return destinationLocation;} public void setDestinationLocation(String d){this.destinationLocation=d;}
    public String getRideDate(){return rideDate;} public void setRideDate(String r){this.rideDate=r;}
    public String getRideTime(){return rideTime;} public void setRideTime(String r){this.rideTime=r;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public Integer getMaxSeats(){return maxSeats;} public void setMaxSeats(Integer m){this.maxSeats=m;}
    public Integer getAvailableSeats(){return availableSeats;} public void setAvailableSeats(Integer a){this.availableSeats=a;}
    public Double getPricePerSeat(){return pricePerSeat;} public void setPricePerSeat(Double p){this.pricePerSeat=p;}
    public String getNotes(){return notes;} public void setNotes(String n){this.notes=n;}
    public String getVehicleType(){return vehicleType;} public void setVehicleType(String v){this.vehicleType=v;}
    public Boolean getAcAvailable(){return acAvailable;} public void setAcAvailable(Boolean a){this.acAvailable=a;}
    public Boolean getLadiesOnly(){return ladiesOnly;} public void setLadiesOnly(Boolean l){this.ladiesOnly=l;}
    public Double getEstimatedDistanceKm(){return estimatedDistanceKm;} public void setEstimatedDistanceKm(Double e){this.estimatedDistanceKm=e;}
    public Integer getEstimatedDurationMin(){return estimatedDurationMin;} public void setEstimatedDurationMin(Integer e){this.estimatedDurationMin=e;}
    public Integer getViewCount(){return viewCount;} public void setViewCount(Integer v){this.viewCount=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
    public PoolUser getUser(){return user;} public void setUser(PoolUser u){this.user=u;}
    public int getAiMatchScore(){return aiMatchScore;} public void setAiMatchScore(int a){this.aiMatchScore=a;}
    public String getAiMatchLabel(){return aiMatchLabel;} public void setAiMatchLabel(String a){this.aiMatchLabel=a;}
}
