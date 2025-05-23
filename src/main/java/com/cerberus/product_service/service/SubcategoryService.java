package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Subcategory;

import java.util.List;
import java.util.Optional;

public interface SubcategoryService {

    SubcategoryDto get(Integer id);


    void create(SubcategoryDto subcategoryDto);

    void update(Integer id, SubcategoryDto subcategoryDto);

    void delete(Integer id);

    Optional<Subcategory> getByTitle(String title);

    List<ProductTypeDto> getProductsTypes(Integer id);
}
