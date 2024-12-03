package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.ProductDto;


public interface ProductService {

    ProductDto get(Integer id);

    void create(ProductDto productDto);

    void update(Integer id, ProductDto productDto);

    void delete(Integer id);
}
