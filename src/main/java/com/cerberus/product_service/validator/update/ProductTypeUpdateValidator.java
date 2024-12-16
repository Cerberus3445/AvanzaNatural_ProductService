package com.cerberus.product_service.validator.update;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.service.ProductTypeService;
import com.cerberus.product_service.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProductTypeUpdateValidator implements Validator {

    @Autowired
    private ProductTypeService productTypeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductTypeDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductTypeDto productTypeDto = (ProductTypeDto) target;
        Optional<ProductType> foundSubcategory= this.productTypeService.getByTitle(productTypeDto.getTitle());

        if(foundSubcategory.isPresent() && !Objects.equals(productTypeDto.getId(), foundSubcategory.get().getId())
                && productTypeDto.getTitle().equalsIgnoreCase(foundSubcategory.get().getTitle())){
            errors.rejectValue("Title", "400","ProductType with this title already exists");
        }
    }
}
