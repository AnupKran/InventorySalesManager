package com.spark.InventorySalesManager.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-ms}")
    private long jwtExpirationMs;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication auth) {
        String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUser(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String roles = claims.get("roles", String.class);
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
