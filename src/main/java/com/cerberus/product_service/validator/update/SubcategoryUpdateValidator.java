package com.cerberus.product_service.validator.update;

import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class SubcategoryUpdateValidator implements Validator {

    @Autowired
    private SubcategoryService subcategoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubcategoryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubcategoryDto subcategoryDto = (SubcategoryDto) target;
        Optional<Subcategory> foundSubcategory= this.subcategoryService.getByTitle(subcategoryDto.getTitle());

        if(foundSubcategory.isPresent() && !Objects.equals(subcategoryDto.getId(), foundSubcategory.get().getId())
                && subcategoryDto.getTitle().equalsIgnoreCase(foundSubcategory.get().getTitle())){
            errors.rejectValue("Title", "400","Subcategory with this title already exists");
        }
    }
}
