package com.example.demo.dto;
import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank(message="Name is required")
    @Size(min=2, max=100, message="Name must be 2-100 characters")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Must be a valid email")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=6, message="Password must be at least 6 characters")
    private String password;

    @NotBlank(message="Role is required")
    @Pattern(regexp="DRIVER|RIDER|BOTH", message="Role must be DRIVER, RIDER or BOTH")
    private String role;

    private String phone;
    private String gender;
    private String homeLocation;
    private String officeLocation;

    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
    public String getRole(){return role;} public void setRole(String r){this.role=r;}
    public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
    public String getGender(){return gender;} public void setGender(String g){this.gender=g;}
    public String getHomeLocation(){return homeLocation;} public void setHomeLocation(String h){this.homeLocation=h;}
    public String getOfficeLocation(){return officeLocation;} public void setOfficeLocation(String o){this.officeLocation=o;}
}
