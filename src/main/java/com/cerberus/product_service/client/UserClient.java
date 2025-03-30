package com.cerberus.product_service.client;

import com.cerberus.product_service.dto.UserDto;

import java.util.Optional;

public interface UserClient {

    Optional<UserDto> getUserByEmail(String email);
}
