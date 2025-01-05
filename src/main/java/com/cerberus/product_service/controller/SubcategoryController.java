package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.SubcategoryService;
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
@RequestMapping("/v1/subcategories")
public class SubcategoryController {

    public final SubcategoryService subcategoryService;

    public final CreateValidator createValidator;

    public final UpdateValidator updateValidator;

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
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(subcategoryDto);

        this.subcategoryService.create(subcategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The subcategory has been created");
    }

    @PatchMapping("/{id}")
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
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.subcategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The subcategory has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
