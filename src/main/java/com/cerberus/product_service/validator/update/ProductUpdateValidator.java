package com.cerberus.product_service.validator.update;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProductUpdateValidator implements Validator {

    @Autowired
    private ProductService productService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDto productDto = (ProductDto) target;
        Optional<Product> foundProduct = this.productService.getByTitle(productDto.getTitle());

        if(foundProduct.isPresent() && !Objects.equals(productDto.getId(), foundProduct.get().getId())
                && productDto.getTitle().equalsIgnoreCase(foundProduct.get().getTitle())){
            errors.rejectValue("Title", "400","Product with this title already exists");
        }
    }
}
