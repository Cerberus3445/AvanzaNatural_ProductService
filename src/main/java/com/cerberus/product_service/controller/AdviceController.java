package com.cerberus.product_service.controller;

import com.cerberus.product_service.exception.CategoryException;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.exception.ProductTypeException;
import com.cerberus.product_service.exception.SubcategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(CategoryException.class)
    public ProblemDetail problemDetail(CategoryException exception){
        ProblemDetail problemDetail;
        if(exception.getMessage().startsWith("404")){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            problemDetail.setTitle("Category not found");
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            problemDetail.setTitle("Category validation exception");
        }
        return problemDetail;
    }

    @ExceptionHandler(ProductException.class)
    public ProblemDetail problemDetail(ProductException exception){
        ProblemDetail problemDetail;
        if(exception.getMessage().startsWith("404")){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            problemDetail.setTitle("Product not found");
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            problemDetail.setTitle("Product validation exception");
        }
        return problemDetail;
    }

    @ExceptionHandler(ProductTypeException.class)
    public ProblemDetail problemDetail(ProductTypeException exception){
        ProblemDetail problemDetail;
        if(exception.getMessage().startsWith("404")){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            problemDetail.setTitle("Product type not found");
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            problemDetail.setTitle("Product type validation exception");
        }
        return problemDetail;
    }

    @ExceptionHandler(SubcategoryException.class)
    public ProblemDetail problemDetail(SubcategoryException exception){
        ProblemDetail problemDetail;
        if(exception.getMessage().startsWith("404")){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            problemDetail.setTitle("Subcategory not found");
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            problemDetail.setTitle("Subcategory validation exception");
        }
        return problemDetail;
    }
}
