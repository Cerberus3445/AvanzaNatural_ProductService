package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.validator.create.CategoryCreateValidator;
import com.cerberus.product_service.validator.update.CategoryUpdateValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public CategoryCreateValidator createValidator;

    @Autowired
    public CategoryUpdateValidator updateValidator;

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable("id") Integer id){
        return this.categoryService.get(id);
    }

    @GetMapping("/{id}/products")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.categoryService.getCategoryProducts(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult){
        this.createValidator.validate(categoryDto, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }

        this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The category has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult){
        this.updateValidator.validate(categoryDto, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }

        this.categoryService.update(id, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body("The category has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The category has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
