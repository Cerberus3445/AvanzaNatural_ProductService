package com.cerberus.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeDto {

    private Integer id;

    @NotBlank(message = "The product type title cannot be empty")
    @Length(min = 2, max = 40, message = "The number of characters of the product type title must be from 2 to 40 characters")
    private String title;
}
