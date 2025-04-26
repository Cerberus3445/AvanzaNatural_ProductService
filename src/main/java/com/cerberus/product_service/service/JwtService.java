package com.cerberus.product_service.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface JwtService {

    Boolean validateToken(String token);

    String extractEmail(String token);

    Collection<? extends GrantedAuthority> extractRole(String token);
}
