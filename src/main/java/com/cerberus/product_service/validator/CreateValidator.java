package com.cerberus.product_service.validator;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.AlreadyExistsException;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.service.ProductService;
import com.cerberus.product_service.service.ProductTypeService;
import com.cerberus.product_service.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateValidator {

    private final CategoryService categoryService;

    private final SubcategoryService subcategoryService;

    private final ProductTypeService productTypeService;

    private final ProductService productService;

    public void validate(CategoryDto categoryDto){
        if(this.categoryService.getByTitle(categoryDto.getTitle()).isPresent()){
            throw new AlreadyExistsException("Category with this title already exists");
        }
    }

    public void validate(SubcategoryDto subcategoryDto){
        if(this.subcategoryService.getByTitle(subcategoryDto.getTitle()).isPresent()){
            throw new AlreadyExistsException("Subcategory with this title already exists");
        }
    }

    public void validate(ProductTypeDto productTypeDto){
        if(this.productTypeService.getByTitle(productTypeDto.getTitle()).isPresent()){
            throw new AlreadyExistsException("ProductType with this title already exists");
        }
    }

    public void validate(ProductDto productDto){
        if(this.productService.getByTitle(productDto.getTitle()).isPresent()){
            throw new AlreadyExistsException("Product with this title already exists");
        }
    }

}
