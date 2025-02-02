package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.ProductTypeService;
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
@RequestMapping("/api/v1/products-types")
@Tag(name = "ProductType Controller", description = "Interaction with products types")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Get product type")
    public ProductTypeDto get(@PathVariable("id") Integer id){
        return this.productTypeService.get(id);
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get all product of the product type with {id}")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.productTypeService.getProductTypeProducts(id);
    }

    @PostMapping
    @Operation(summary = "Create product type")
    public ResponseEntity<String> create(@RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(productTypeDto);

        this.productTypeService.create(productTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The product type has been created");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update product type")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        productTypeDto.setId(id); //for update validation
        this.updateValidator.validate(productTypeDto);

        this.productTypeService.update(id, productTypeDto);
        return ResponseEntity.status(HttpStatus.OK).body("The product type has been updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product type")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The product type has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
