package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="notifications")
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id") private PoolUser user;
    private String title;
    private String message;
    private String type; // RIDE_BOOKED, RIDE_CANCELLED, PAYMENT, RATING, SYSTEM, ECO
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    @PrePersist void onCreate(){ createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public PoolUser getUser(){return user;} public void setUser(PoolUser u){this.user=u;}
    public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
    public String getMessage(){return message;} public void setMessage(String m){this.message=m;}
    public String getType(){return type;} public void setType(String t){this.type=t;}
    public Boolean getIsRead(){return isRead;} public void setIsRead(Boolean r){this.isRead=r;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}
