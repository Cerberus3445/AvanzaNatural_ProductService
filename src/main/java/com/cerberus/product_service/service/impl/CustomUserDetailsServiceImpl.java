package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.client.UserClient;
import com.cerberus.product_service.dto.UserDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.model.UserCredential;
import com.cerberus.product_service.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserClient userClient;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = this.userClient.getUserByEmail(username).orElseThrow(
                NotFoundException::new
        );

        return new UserCredential(user);
    }
}
