package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.ProductService;
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

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Get product")
    public ProductDto get(@PathVariable("id") Integer id){
        return this.productService.get(id);
    }

    @GetMapping("/category/{categoryId}/pagination")
    public List<ProductDto> getByCategoryWithPagination(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                            @RequestParam(value = "size",defaultValue = "10") Integer size,
                                            @PathVariable("categoryId") Integer categoryId){
        return this.productService.getByCategory(categoryId,page, size);
    }

    @GetMapping("/subcategory/{categoryId}/pagination")
    public List<ProductDto> getBySubcategoryWithPagination(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                            @RequestParam(value = "size",defaultValue = "10") Integer size,
                                            @PathVariable("categoryId") Integer categoryId){
        return this.productService.getBySubcategory(categoryId,page, size);
    }

    @GetMapping("/product-type/{categoryId}/pagination")
    public List<ProductDto> getByProductTypeWithPagination(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                            @RequestParam(value = "size",defaultValue = "10") Integer size,
                                            @PathVariable("categoryId") Integer categoryId){
        return this.productService.getByProductType(categoryId,page, size);
    }

    @PostMapping
    @Operation(summary = "Create product")
    public ResponseEntity<String> create(@RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(productDto);

        this.productService.create(productDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("The product has been created.");
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
        return ResponseEntity.status(HttpStatus.OK).body("The product has been updated.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The product has been deleted.");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString().replace("[", "").replace("]", "");
    }
}
