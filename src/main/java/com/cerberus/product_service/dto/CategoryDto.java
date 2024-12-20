package com.cerberus.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {

    private Integer id;

    @NotBlank(message = "The category title cannot be empty")
    @Length(min = 2, max = 40, message = "The number of characters of the category title must be from 2 to 40 characters")
    private String title;
}
