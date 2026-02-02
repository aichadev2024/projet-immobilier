package com.projetimmo.projet_immobilier.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "super-secret-key-super-secret-key-super-secret-key"; // ≥ 32 chars

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ GÉNÉRATION DU TOKEN
    public String generateToken(String nomUtilisateur) {
        return Jwts.builder()
                .setSubject(nomUtilisateur)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    // ✅ EXTRACTION DU USERNAME
    public String extractNomUtilisateur(String token) {
        return extractClaims(token).getSubject();
    }

    // 🔐 VALIDATION CORRECTE (AVEC UserDetails)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractNomUtilisateur(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // ⏰ EXPIRATION
    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // 🔍 CLAIMS
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
