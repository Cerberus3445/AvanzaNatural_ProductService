package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.service.ProductService;
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
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    public ProductService productService;

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable("id") Integer id){
        return this.productService.get(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }
        this.productService.create(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The product has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }
        this.productService.update(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body("The product has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The product has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
