package com.cerberus.product_service.controller;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.AlreadyExistsException;
import com.cerberus.product_service.exception.ValidationException;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.service.StorageService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Controller", description = "Interaction with categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    private final StorageService storageService;

    @GetMapping("/{id}")
    @Operation(summary = "Get category")
    public CategoryDto get(@PathVariable("id") Integer id){
        return this.categoryService.get(id);
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get all products of the category with {id}")
    public List<ProductDto> getProducts(@PathVariable("id") Integer id){
        return this.categoryService.getCategoryProducts(id);
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public List<CategoryDto> getCategories(){
        return this.categoryService.getAll();
    }

    @GetMapping("/{id}/subcategories")
    @Operation(summary = "Get all subcategories of the category with {id}")
    public List<SubcategoryDto> getSubcategories(@PathVariable("id") Integer id){
        return this.categoryService.getSubcategories(id);
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<String> create(@RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult
//                                         @RequestParam(value = "image") MultipartFile image
    ){
        if(bindingResult.hasErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.createValidator.validate(categoryDto);

        if(bindingResult.hasErrors()) throw new AlreadyExistsException("Category");

        this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The category has been created");
    }

//    @PostMapping("/{id}/upload")
//    public ResponseEntity<String> uploadFile(@PathVariable("id") Integer id,
//                                             @RequestParam(value = "image") MultipartFile image) {
//        String fileName = this.storageService.uploadFile(image);
//
//    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update category")
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
    @Operation(summary = "Delete category")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("The category has been deleted");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
