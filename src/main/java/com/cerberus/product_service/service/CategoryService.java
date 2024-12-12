package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto get(Integer id);

    List<ProductDto> getCategoryProducts(Integer categoryId);

    void create(CategoryDto categoryDto);

    void update(Integer id, CategoryDto categoryDto);

    void delete(Integer id);
}
