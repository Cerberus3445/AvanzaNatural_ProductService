package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.service.ProductTypeService;
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
@RequestMapping("/v1/productstypes")
public class ProductTypeController {

    @Autowired
    public ProductTypeService productTypeService;

    @GetMapping("/{id}")
    public ProductTypeDto get(@PathVariable("id") Integer id){
        return this.productTypeService.get(id);
    }

    @GetMapping("/{id}/products")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.productTypeService.getProductTypeProducts(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }
        this.productTypeService.create(productTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The product type has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ProductException(collectErrorsToString(bindingResult.getFieldErrors()));
        }
        this.productTypeService.update(id, productTypeDto);
        return ResponseEntity.status(HttpStatus.OK).body("The product type has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The product type has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
