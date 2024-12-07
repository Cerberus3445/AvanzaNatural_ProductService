package com.cerberus.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AvanzaNaturalProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvanzaNaturalProductServiceApplication.class, args);
    }

}
