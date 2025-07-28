package com.seek.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Servicio utilitario para generar y validar JSON Web Tokens (HS256).
 * <p>La clave se inyecta desde <i>application.yaml</i> / variable de
 * entorno: {@code security.jwt.secret}</p>
 */
@Service
public class JwtService {

    /** Se recomienda una clave de 256 bits (32 chars base64). */
    @Value("${security.jwt.secret}")
    private String secretKey;

    /** (Opcional) tiempo de expiración en minutos */
    @Value("${security.jwt.expiration-minutes:60}")
    private Integer expirationMinutes;

    /* ---------- API pública ---------- */

    public String createToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMinutes * 60_000L);

        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parse(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return parse(token).getSubject();
    }

    /* ---------- Helpers ---------- */

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
