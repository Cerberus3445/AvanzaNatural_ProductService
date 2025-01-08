package com.cerberus.product_service.dto;

import jakarta.validation.constraints.NotBlank;
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

    private Integer categoryId;

    @NotBlank(message = "The subcategory title cannot be empty")
    @Length(min = 2, max = 40, message = "The number of characters of the subcategory title must be from 2 to 40 characters")
    private String title;
}
