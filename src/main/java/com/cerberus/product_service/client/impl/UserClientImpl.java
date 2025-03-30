package com.cerberus.product_service.client.impl;

import com.cerberus.product_service.client.UserClient;
import com.cerberus.product_service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:8080/api/v1/users")
            .build();

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        String path = "/email";
        String query = "email={email}";
        return Optional.ofNullable(this.restClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(email))
                .retrieve()
                .body(UserDto.class));
    }
}
