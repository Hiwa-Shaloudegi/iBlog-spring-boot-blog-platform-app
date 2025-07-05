package dev.hiwa.iblog.security;

import dev.hiwa.iblog.domain.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private int refreshTokenExpiration;


    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenExpiration);
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    public String extractEmail(String token) {
        Object email = extractClaims(token).get("email");
        return email != null ? email.toString() : null;
    }

    public String extractName(String token) {
        Object email = extractClaims(token).get("email");
        return email != null ? email.toString() : null;
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }


    private String buildToken(User user, long expirationTime) {
        Claims claims = Jwts
                .claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * expirationTime))
                .build();

        return Jwts.builder().claims(claims).signWith(getSignInKey()).compact();
    }


    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


}
