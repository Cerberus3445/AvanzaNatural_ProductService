package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.exception.CategoryException;
import com.cerberus.product_service.exception.ProductTypeException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.repository.ProductTypeRepository;
import com.cerberus.product_service.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "productType", key = "#id")
    public ProductTypeDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.productTypeRepository.findById(id)
                .orElseThrow(() -> new ProductTypeException("404. ProductType with %d id not found.".formatted(id))));
    }

    @Override
    @Cacheable(value = "productTypeProducts", key = "#id")
    public List<ProductDto> getProductTypeProducts(Integer id) {
        log.info("getProductTypeProducts {}", id);
        ProductType productType =  this.productTypeRepository.findById(id)
                .orElseThrow(() -> new ProductTypeException("404.ProductType with %d id not found.".formatted(id)));
        return mapper.toDto(productType.getProducts());
    }

    @Override
    @Transactional
    public void create(ProductTypeDto productTypeDto) {
        log.info("create {}", productTypeDto);
        this.productTypeRepository.save(this.mapper.toEntity(productTypeDto));
    }

    @Override
    @CacheEvict(value = "productType", key = "#id")
    @Transactional
    public void update(Integer id, ProductTypeDto productTypeDto) {
        log.info("update {}, {}", id, productTypeDto);
        this.productTypeRepository.findById(id).ifPresentOrElse(productType -> {
            ProductType updatedProductType = ProductType.builder()
                    .id(id)
                    .title(productTypeDto.getTitle())
                    .products(productType.getProducts())
                    .build();
            this.productTypeRepository.save(updatedProductType);
        }, () -> {
            throw new CategoryException("404.ProductType with %d id not found.".formatted(id));
        });
    }

    @Override
    @CacheEvict(value = "productType", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        this.productTypeRepository.deleteById(id);
    }

    @Override
    public Optional<ProductType> getByTitle(String title) {
        return this.productTypeRepository.findByTitle(title);
    }

}
