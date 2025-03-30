package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    ProductDto get(Integer id);

    void create(ProductDto productDto);

    void update(Integer id, ProductDto productDto);

    void delete(Integer id);

    Optional<Product> getByTitle(String title);

    List<ProductDto> getByCategory(Integer categoryId, Integer page, Integer size);

    List<ProductDto> getBySubcategory(Integer subcategoryId, Integer page, Integer size);

    List<ProductDto> getByProductType(Integer productTypeId, Integer page, Integer size);
}
