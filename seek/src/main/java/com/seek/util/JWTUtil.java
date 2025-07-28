package com.seek.util;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Component
public class JWTUtil {

    private String secretKey = "dba34dbb3cf4bfeeab9076f7318826d3e739d08061e029b49df14c7ecb20bd6d5ea419ad5537df9c045d0e9cb8f0ff439510688bce9c376e7a0ed7da022692058f4d9cb281e63ae6c0f64ecf80e39a035e590516e520e4421b0fd80b4f24eb88a9a8b2e4b9ac7114ebf5fc56237dff0b3d40b6a4a31e328eb339d01b015d1ca9";
    private final String superRole = "ADMIN";

    public JWTUtil() {

    }

    public String generateToken() {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean verifyRole(String jwt, String roleRequest) {
        String role = extractRole(jwt);
        return role != null && (role.equals(roleRequest) || role.equals(superRole));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Token is invalid if any exception occurs during parsing
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
