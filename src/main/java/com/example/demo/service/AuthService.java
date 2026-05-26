package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.PoolUser;
import com.example.demo.repository.PoolUserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private PoolUserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req){
        if(userRepo.findByEmail(req.getEmail()).isPresent())
            throw new RuntimeException("Email already registered. Please login instead.");

        PoolUser user = new PoolUser();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setPhone(req.getPhone());
        user.setGender(req.getGender());
        user.setHomeLocation(req.getHomeLocation());
        user.setOfficeLocation(req.getOfficeLocation());
        user.setIsActive(true);
        user.setRating(5.0);
        user.setEcoPoints(0);
        user.setTotalRides(0);

        PoolUser saved = userRepo.save(user);
        String token = jwtUtil.generateToken(saved.getId(), saved.getEmail(), saved.getRole());
        return new AuthResponse(token, saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(AuthRequest req){
        // Built-in admin account
        if ("admin@poolai.com".equals(req.getEmail()) && "admin123".equals(req.getPassword())) {
            String token = jwtUtil.generateToken(0L, "admin@poolai.com", "ADMIN");
            return new AuthResponse(token, 0L, "System Admin", "admin@poolai.com", "ADMIN");
        }

        PoolUser user = userRepo.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("No account found with this email. Please register first."));

        if (!Boolean.TRUE.equals(user.getIsActive()))
            throw new RuntimeException("Your account has been deactivated. Contact support.");

        // Handle users registered via old /pool/add endpoint (no password set)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // Allow login without password for legacy users - just verify email exists
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
            return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect password. Please try again.");

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
