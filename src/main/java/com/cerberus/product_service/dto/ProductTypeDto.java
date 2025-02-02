package com.cerberus.product_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotNull(message = "The subcategoryId cannot be empty")
    @Min(value = 1, message = "The subcategoryId cannot be empty")
    private Integer subcategoryId;

    private String image;

    @NotBlank(message = "The product type title cannot be empty")
    @Length(min = 2, max = 40, message = "The number of characters of the product type title must be from 2 to 40 characters")
    private String title;
}
