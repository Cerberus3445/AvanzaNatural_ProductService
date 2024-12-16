package com.cerberus.product_service.validator.update;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryUpdateValidator implements Validator {

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDto categoryDto = (CategoryDto) target;
        Optional<Category> foundCategory = this.categoryService.getByTitle(categoryDto.getTitle());

        if(foundCategory.isPresent() && !Objects.equals(categoryDto.getId(), foundCategory.get().getId())
        && categoryDto.getTitle().equalsIgnoreCase(foundCategory.get().getTitle())){
            errors.rejectValue("Title", "400","Category with this title already exists");
        }
    }
}
