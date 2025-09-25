package com.example.profilehunter.service.auth.jwt;

import com.example.profilehunter.model.dto.auth.AuthRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getExpirationFromJwtToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationFromJwtToken(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromToken(token);

        return  claimsResolver.apply(claims);
    }

    private Claims getClaimsFromToken(String token) {
        return (Claims) Jwts.parser().verifyWith(key()).build().parse(token).getPayload();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = getUserNameFromJwtToken(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date currentDate = new Date();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String generateJwtToken(AuthRequest authRequest) {
        Date currentDate = new Date();

        return Jwts.builder()
                .subject(authRequest.getUsername())
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

}
