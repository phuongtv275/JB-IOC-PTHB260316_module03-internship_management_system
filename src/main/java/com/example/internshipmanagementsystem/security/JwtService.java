package com.example.internshipmanagementsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey signingKey;
  private final long expirationMs;

  public JwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationMs) {
    this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    this.expirationMs = expirationMs;
  }

  public String generateToken(UserDetails userDetails) {
    Date issuedAt = new Date();
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(issuedAt)
        .expiration(new Date(issuedAt.getTime() + expirationMs))
        .signWith(signingKey)
        .compact();
  }

  public String extractUsername(String token) {
    return claims(token).getSubject();
  }

  private Claims claims(String token) {
    return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
  }
}
