package com.cerberus.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Boolean emailConfirmed;

    private Role role;
}
