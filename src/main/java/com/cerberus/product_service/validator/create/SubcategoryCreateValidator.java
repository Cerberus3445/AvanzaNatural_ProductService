package com.cerberus.product_service.validator.create;

import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SubcategoryCreateValidator implements Validator {

    @Autowired
    private SubcategoryService subcategoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubcategoryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubcategoryDto subcategoryDto = (SubcategoryDto) target;
        if(this.subcategoryService.getByTitle(subcategoryDto.getTitle()).isPresent()){

            errors.rejectValue("Title", "400","Subcategory with this title already exists");
        }
    }
}
