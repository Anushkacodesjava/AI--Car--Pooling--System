package com.example.demo.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String name;
    private String email;
    private String role;

    public AuthResponse(String token, Long userId, String name, String email, String role){
        this.token=token; this.userId=userId; this.name=name; this.email=email; this.role=role;
    }
    public String getToken(){return token;} public void setToken(String t){this.token=t;}
    public String getType(){return type;}
    public Long getUserId(){return userId;} public void setUserId(Long u){this.userId=u;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getRole(){return role;} public void setRole(String r){this.role=r;}
}
