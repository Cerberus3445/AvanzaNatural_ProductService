package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.service.SubcategoryService;
import com.cerberus.product_service.validator.create.SubcategoryCreateValidator;
import com.cerberus.product_service.validator.update.SubcategoryUpdateValidator;
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
@RequestMapping("/v1/subcategories")
public class SubcategoryController {

    @Autowired
    public SubcategoryService subcategoryService;

    @Autowired
    public SubcategoryCreateValidator createValidator;

    @Autowired
    public SubcategoryUpdateValidator updateValidator;

    @GetMapping("/{id}")
    public SubcategoryDto get(@PathVariable("id") Integer id){
        return this.subcategoryService.get(id);
    }

    @GetMapping("/{id}/products")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.subcategoryService.getSubcategoryProducts(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        this.createValidator.validate(subcategoryDto, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }

        this.subcategoryService.create(subcategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The subcategory has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        this.updateValidator.validate(subcategoryDto, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }

        this.subcategoryService.update(id, subcategoryDto);
        return ResponseEntity.status(HttpStatus.OK).body("The subcategory has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.subcategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The subcategory has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
