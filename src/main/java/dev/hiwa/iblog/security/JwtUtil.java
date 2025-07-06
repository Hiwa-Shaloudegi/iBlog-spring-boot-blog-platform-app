package dev.hiwa.iblog.security;

import dev.hiwa.iblog.domain.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private int refreshTokenExpiration;


    public String generateAccessToken(User user) {
        return _buildToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return _buildToken(user, refreshTokenExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractEmail(token).equals(userDetails.getUsername()) && !_isTokenExpired(token);
    }

    public String extractEmail(String token) {
        Object email = _extractClaims(token).get("email");
        return email != null ? email.toString() : null;
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(_extractClaims(token).getSubject());
    }

    public String extractName(String token) {
        Object email = _extractClaims(token).get("email");
        return email != null ? email.toString() : null;
    }

    private boolean _isTokenExpired(String token) {
        return _extractClaims(token).getExpiration().before(new Date());
    }


    private String _buildToken(User user, long expirationTime) {
        Claims claims = Jwts
                .claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * expirationTime))
                .build();

        return Jwts.builder().claims(claims).signWith(_getSignInKey()).compact();
    }


    private Claims _extractClaims(String token) {
        return Jwts.parser().verifyWith(_getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey _getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


}
