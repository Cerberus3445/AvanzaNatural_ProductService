package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final String SECRET = "5367566B5970337336762342342342341139792F4123F452811482B4D6251655468576D5A71347437";

    @Override
    public Boolean validateToken(String token) {
        Map<String, Object> claims = extractAllClaims(token);
        String emailConfirmed = claims.get("emailConfirmed").toString();
        String role = claims.get("role").toString();
        return Objects.equals(emailConfirmed, "true") && Objects.equals(role, "ROLE_ADMIN");
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Collection<? extends GrantedAuthority> extractRole(String token) {
        Map<String, Object> claims = extractAllClaims(token);
        return Collections.singleton(new SimpleGrantedAuthority(claims.get("role").toString()));
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
