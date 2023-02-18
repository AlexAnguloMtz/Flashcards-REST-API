package com.aram.auth.security;

import com.aram.auth.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.lang.System.currentTimeMillis;

@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String tokenFor(AppUser user) {
        return generateToken(user);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(AppUser user) {
        return Jwts.builder()
                   .setClaims(claimsFrom(user))
                   .setSubject(user.getUsername())
                   .setIssuedAt(new Date(currentTimeMillis()))
                   .setExpiration(new Date(currentTimeMillis() + 1000 * 60 * 24))
                   .signWith(getSigningKey(), HS256)
                   .compact();
    }

    private Map<String, ?> claimsFrom(AppUser user) {
        return Map.of(
                "email", user.getEmail(),
                "role", user.getRole()
        );
    }

    public boolean isValidToken(String token) {
        try {
            return (!isExpired(token));
        } catch (Exception exception) {
            return false;
        }
    }

    private boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
