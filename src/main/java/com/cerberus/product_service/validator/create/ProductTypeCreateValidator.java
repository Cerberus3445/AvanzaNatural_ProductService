package com.cerberus.product_service.validator.create;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.service.ProductTypeService;
import com.cerberus.product_service.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductTypeCreateValidator implements Validator {

    @Autowired
    private ProductTypeService productTypeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductTypeDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductTypeDto productTypeDto = (ProductTypeDto) target;
        if(this.productTypeService.getByTitle(productTypeDto.getTitle()).isPresent()){

            errors.rejectValue("Title", "400","ProductType with this title already exists");
        }
    }
}
