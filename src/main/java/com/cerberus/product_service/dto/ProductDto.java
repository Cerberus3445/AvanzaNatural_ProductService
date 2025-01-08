package com.cerberus.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotBlank(message = "The product title cannot be empty")
    @Length(min = 2, max = 60, message = "The number of characters of the product title must be from 2 to 60 characters")
    private String title;

    @NotBlank(message = "The brand cannot be empty")
    @Length(min = 2, max = 60, message = "The number of characters of the brand must be from 2 to 60 characters")
    private String brand;

    @NotBlank(message = "The product description cannot be empty")
    @Length(max = 1000, message = "The maximum number of characters of the product description is 1000")
    private String description;

    @NotNull(message = "The product price cannot be empty")
    @Positive(message = "The price of the product cannot be negative")
    private Double price;

    @NotNull(message = "The product inStock status cannot be empty")
    private Boolean inStock;

    private Integer categoryId;

    private Integer subcategoryId;

    private Integer productTypeId;
}
