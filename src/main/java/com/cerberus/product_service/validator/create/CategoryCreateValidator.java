package com.cerberus.product_service.validator.create;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryCreateValidator implements Validator {

    @Autowired
    private CategoryService categoryService;


    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDto categoryDto = (CategoryDto) target;
        if(this.categoryService.getByTitle(categoryDto.getTitle()).isPresent()){

            errors.rejectValue("Title", "400","Category with this title already exists");
        }
    }
}
