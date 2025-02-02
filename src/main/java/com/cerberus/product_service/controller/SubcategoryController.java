package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.SubcategoryService;
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
@RequestMapping("/api/v1/subcategories")
@Tag(name = "Subcategory Controller", description = "Interaction with subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Get subcategory")
    public SubcategoryDto get(@PathVariable("id") Integer id){
        return this.subcategoryService.get(id);
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get all products of the subcategory with {id}")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.subcategoryService.getSubcategoryProducts(id);
    }

    @GetMapping("/{id}/products-types")
    @Operation(summary = "Get all products types of the subcategory id with {id}")
    public List<ProductTypeDto> getProductsTypes(@PathVariable("id") Integer id){
        return this.subcategoryService.getProductsTypes(id);
    }

    @PostMapping
    @Operation(summary = "Create subcategory")
    public ResponseEntity<String> create(@RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(subcategoryDto);

        this.subcategoryService.create(subcategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The subcategory has been created");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update subcategory")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        subcategoryDto.setId(id); //for update validation
        this.updateValidator.validate(subcategoryDto);

        this.subcategoryService.update(id, subcategoryDto);
        return ResponseEntity.status(HttpStatus.OK).body("The subcategory has been updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subcategory")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.subcategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The subcategory has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
