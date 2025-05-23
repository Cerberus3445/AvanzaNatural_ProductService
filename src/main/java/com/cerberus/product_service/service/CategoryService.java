package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDto get(Integer id);

    List<CategoryDto> getAll();

    void create(CategoryDto categoryDto);

    void update(Integer id, CategoryDto categoryDto);

    void delete(Integer id);

    Optional<Category> getByTitle(String title);

    List<SubcategoryDto> getSubcategories(Integer id);
}
