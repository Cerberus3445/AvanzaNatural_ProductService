package com.cerberus.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotNull(message = "The categoryId cannot be empty")
    @Min(value = 1, message = "The categoryId cannot be less than 1")
    private Integer categoryId;

    private String image;

    @NotBlank(message = "The subcategory title cannot be empty")
    @Length(min = 2, max = 40, message = "The number of characters of the subcategory title must be from 2 to 40 characters")
    private String title;
}
