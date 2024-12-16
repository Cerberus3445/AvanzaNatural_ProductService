package com.cerberus.product_service.service;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductTypeService {

    ProductTypeDto get(Integer id);

    List<ProductDto> getProductTypeProducts(Integer productTypeId);

    void create(ProductTypeDto productTypeDto);

    void update(Integer id, ProductTypeDto productTypeDto);

    void delete(Integer id);

    Optional<ProductType> getByTitle(String title);
}
