package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.AlreadyExistsException;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.validator.CreateValidator;
import com.cerberus.product_service.validator.UpdateValidator;
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
@RequestMapping("/v1/categories")
public class CategoryController {

    public final CategoryService categoryService;

    public final CreateValidator createValidator;

    public final UpdateValidator updateValidator;

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
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(categoryDto);

        if(bindingResult.hasErrors()) throw new AlreadyExistsException("Category");

        this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The category has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        categoryDto.setId(id); //for update validation
        this.updateValidator.validate(categoryDto);

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
