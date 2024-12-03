package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.model.Product;


public interface ProductService {

    ProductDto get(Integer id);

    void create(ProductDto productDto);

    void update(Integer id, ProductDto productDto);

    void delete(Integer id);
}
