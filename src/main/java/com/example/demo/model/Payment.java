package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "payment")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne @JoinColumn(name="participation_id") private RideParticipation participation;
    private double amount;
    private String status; // PENDING, SUCCESS, FAILED, REFUNDED
    private String method; // UPI, CARD, CASH, WALLET
    private String transactionId;
    private LocalDateTime paymentTime;
    @PrePersist void onCreate(){ paymentTime=LocalDateTime.now(); transactionId="TXN"+System.currentTimeMillis(); }

    public int getId(){return id;} public void setId(int id){this.id=id;}
    public RideParticipation getParticipation(){return participation;} public void setParticipation(RideParticipation p){this.participation=p;}
    public double getAmount(){return amount;} public void setAmount(double a){this.amount=a;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public String getMethod(){return method;} public void setMethod(String m){this.method=m;}
    public String getTransactionId(){return transactionId;} public void setTransactionId(String t){this.transactionId=t;}
    public LocalDateTime getPaymentTime(){return paymentTime;} public void setPaymentTime(LocalDateTime p){this.paymentTime=p;}
}
