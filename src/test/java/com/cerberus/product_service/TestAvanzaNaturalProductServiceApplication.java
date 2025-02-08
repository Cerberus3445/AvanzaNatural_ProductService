package com.cerberus.product_service;

import org.springframework.boot.SpringApplication;

public class TestAvanzaNaturalProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AvanzaNaturalProductServiceApplication::main).
                with(TestcontainersConfiguration.class)
                .run(args);
    }

}
