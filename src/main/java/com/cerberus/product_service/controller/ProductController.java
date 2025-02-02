package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.ProductService;
import com.cerberus.product_service.util.CacheClear;
import com.cerberus.product_service.validator.CreateValidator;
import com.cerberus.product_service.validator.UpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Interaction with products")
public class ProductController {

    private final ProductService productService;

    private final CacheClear clearCache;

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Get product")
    public ProductDto get(@PathVariable("id") Integer id){
        return this.productService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create product")
    public ResponseEntity<String> create(@RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(productDto);

        this.productService.create(productDto);

        clearCache(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The product has been created");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        productDto.setId(id); //for update validation
        this.updateValidator.validate(productDto);

        this.productService.update(id, productDto);
        clearCache(productDto);
        return ResponseEntity.status(HttpStatus.OK).body("The product has been updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        ProductDto productDto = this.productService.get(id);
        this.productService.delete(id);
        clearCache(productDto);
        return ResponseEntity.status(HttpStatus.OK).body("The product has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }

    public void clearCache(ProductDto productDto){
        this.clearCache.clearCategoryProducts(productDto.getCategoryId());
        this.clearCache.clearSubcategoryProducts(productDto.getSubcategoryId());
        this.clearCache.clearProductTypeProducts(productDto.getProductTypeId());
    }
}
