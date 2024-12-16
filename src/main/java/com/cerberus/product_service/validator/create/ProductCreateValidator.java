package com.cerberus.product_service.validator.create;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductCreateValidator implements Validator {

    @Autowired
    private ProductService productService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDto productDto = (ProductDto) target;
        if(this.productService.getByTitle(productDto.getTitle()).isPresent()){
            errors.rejectValue("Title", "400","Product with this title already exists");
        }
    }
}
