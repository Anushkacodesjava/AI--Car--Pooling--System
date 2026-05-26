package com.example.demo.model;
import jakarta.persistence.*;

@Entity @Table(name = "vehicles")
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vehicleNumber;
    private String vehicleType;
    private String brand;
    private String color;
    private int seats;
    private Boolean acAvailable = false;
    private Boolean isVerified = false;
    @ManyToOne @JoinColumn(name="user_id") private PoolUser owner;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){this.vehicleNumber=v;}
    public String getVehicleType(){return vehicleType;} public void setVehicleType(String v){this.vehicleType=v;}
    public String getBrand(){return brand;} public void setBrand(String b){this.brand=b;}
    public String getColor(){return color;} public void setColor(String c){this.color=c;}
    public int getSeats(){return seats;} public void setSeats(int s){this.seats=s;}
    public Boolean getAcAvailable(){return acAvailable;} public void setAcAvailable(Boolean a){this.acAvailable=a;}
    public Boolean getIsVerified(){return isVerified;} public void setIsVerified(Boolean v){this.isVerified=v;}
    public PoolUser getOwner(){return owner;} public void setOwner(PoolUser o){this.owner=o;}
}
