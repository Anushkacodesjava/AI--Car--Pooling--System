package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pool_users",
    indexes = {
        @Index(name = "idx_user_email",  columnList = "email",    unique = true),
        @Index(name = "idx_user_active", columnList = "isActive")
    })
public class PoolUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100) private String name;

    @NotBlank @Email @Column(unique = true) private String email;

    private String password;
    private String phone;
    private String gender;

    @NotBlank private String role;
    private String homeLocation;
    private String officeLocation;
    private Double rating = 5.0;
    private Integer totalRides = 0;
    private Integer totalRidesOffered = 0;
    private Integer ecoPoints = 0;
    private Boolean isActive = true;
    private Boolean isVerified = false;
    private String preferredPayment;
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }
    public String getName()                      { return name; }
    public void setName(String n)                { this.name = n; }
    public String getEmail()                     { return email; }
    public void setEmail(String e)               { this.email = e; }
    public String getPassword()                  { return password; }
    public void setPassword(String p)            { this.password = p; }
    public String getPhone()                     { return phone; }
    public void setPhone(String p)               { this.phone = p; }
    public String getGender()                    { return gender; }
    public void setGender(String g)              { this.gender = g; }
    public String getRole()                      { return role; }
    public void setRole(String r)                { this.role = r; }
    public String getHomeLocation()              { return homeLocation; }
    public void setHomeLocation(String h)        { this.homeLocation = h; }
    public String getOfficeLocation()            { return officeLocation; }
    public void setOfficeLocation(String o)      { this.officeLocation = o; }
    public Double getRating()                    { return rating; }
    public void setRating(Double r)              { this.rating = r; }
    public Integer getTotalRides()               { return totalRides; }
    public void setTotalRides(Integer t)         { this.totalRides = t; }
    public Integer getTotalRidesOffered()        { return totalRidesOffered; }
    public void setTotalRidesOffered(Integer t)  { this.totalRidesOffered = t; }
    public Integer getEcoPoints()               { return ecoPoints; }
    public void setEcoPoints(Integer e)          { this.ecoPoints = e; }
    public Boolean getIsActive()                 { return isActive; }
    public void setIsActive(Boolean a)           { this.isActive = a; }
    public Boolean getIsVerified()               { return isVerified; }
    public void setIsVerified(Boolean v)         { this.isVerified = v; }
    public String getPreferredPayment()          { return preferredPayment; }
    public void setPreferredPayment(String p)    { this.preferredPayment = p; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime c)    { this.createdAt = c; }
}
