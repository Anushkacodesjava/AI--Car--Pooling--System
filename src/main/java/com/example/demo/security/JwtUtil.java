package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getKey(){ return Keys.hmacShaKeyFor(secret.getBytes()); }

    public String generateToken(Long userId, String email, String role){
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
            .parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token){ return extractClaims(token).getSubject(); }
    public Long extractUserId(String token){ return extractClaims(token).get("userId",Long.class); }
    public String extractRole(String token){ return extractClaims(token).get("role",String.class); }

    public boolean isValid(String token){
        try{ extractClaims(token); return true; }
        catch(Exception e){ return false; }
    }
}
