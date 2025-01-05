package com.cerberus.product_service.validator;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.AlreadyExistsException;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.service.ProductService;
import com.cerberus.product_service.service.ProductTypeService;
import com.cerberus.product_service.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateValidator {

    private final CategoryService categoryService;

    private final SubcategoryService subcategoryService;

    private final ProductTypeService productTypeService;

    private final ProductService productService;

    public void validate(CategoryDto categoryDto){
        Optional<Category> foundCategory = this.categoryService.getByTitle(categoryDto.getTitle());

        if(foundCategory.isPresent() && !Objects.equals(categoryDto.getId(), foundCategory.get().getId())
                && categoryDto.getTitle().equalsIgnoreCase(foundCategory.get().getTitle())){
            throw new AlreadyExistsException("Category with this title already exists");
        }
    }

    public void validate(SubcategoryDto subcategoryDto){
        Optional<Subcategory> foundSubcategory= this.subcategoryService.getByTitle(subcategoryDto.getTitle());

        if(foundSubcategory.isPresent() && !Objects.equals(subcategoryDto.getId(), foundSubcategory.get().getId())
                && subcategoryDto.getTitle().equalsIgnoreCase(foundSubcategory.get().getTitle())){
            throw new AlreadyExistsException("Subcategory with this title already exists");
        }
    }

    public void validate(ProductTypeDto productTypeDto){
        Optional<ProductType> foundSubcategory= this.productTypeService.getByTitle(productTypeDto.getTitle());

        if(foundSubcategory.isPresent() && !Objects.equals(productTypeDto.getId(), foundSubcategory.get().getId())
                && productTypeDto.getTitle().equalsIgnoreCase(foundSubcategory.get().getTitle())){
            throw new AlreadyExistsException("ProductType with this title already exists");
        }
    }

    public void validate(ProductDto productDto){
        Optional<Product> foundProduct = this.productService.getByTitle(productDto.getTitle());

        if(foundProduct.isPresent() && !Objects.equals(productDto.getId(), foundProduct.get().getId())
                && productDto.getTitle().equalsIgnoreCase(foundProduct.get().getTitle())){
            throw new AlreadyExistsException("Product with this title already exists");
        }
    }
}
