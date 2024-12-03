package com.cerberus.product_service.dto;

import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.model.Subcategory;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Integer id;

    @NotBlank(message = "The product title cannot be empty")
    @Length(min = 2, max = 60, message = "The number of characters of the product title must be from 2 to 60 characters")
    private String title;

    @NotBlank(message = "The product description cannot be empty")
    @Length(max = 1000, message = "The maximum number of characters of the product description is 1000")
    private String description;

    @NotNull(message = "The product price cannot be empty")
    @Positive(message = "The price of the product cannot be negative")
    private Double price;

    @NotNull(message = "The product inStock status cannot be empty")
    private Boolean inStock;

    private CategoryDto category;

    private ProductTypeDto productType;

    private SubcategoryDto subcategory;
}
