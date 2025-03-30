package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.cache.CacheClear;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.ProductTypeMapper;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.repository.ProductTypeRepository;
import com.cerberus.product_service.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "productType", key = "#id")
    public ProductTypeDto get(Integer id) {
        log.info("get {}", id);
        return this.productTypeMapper.toDto(this.productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType",id)));
    }

    @Override
    @Transactional
    public void create(ProductTypeDto productTypeDto) {
        log.info("create {}", productTypeDto);
        this.productTypeRepository.save(this.productTypeMapper.toEntity(productTypeDto));
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
                    .subcategory(productType.getSubcategory())
                    .products(productType.getProducts())
                    .build();
            this.productTypeRepository.save(updatedProductType);
            this.cacheClear.clearProductsTypesOfCertainSubcategory(productTypeDto.getSubcategoryId());
        }, () -> {
            throw new NotFoundException("ProductType",id);
        });
    }

    @Override
    @CacheEvict(value = "productType", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        ProductType productType = this.productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType",id));
        this.productTypeRepository.deleteById(id);
        this.cacheClear.clearProductsTypesOfCertainSubcategory(productType.getSubcategory().getId());
    }

    @Override
    public Optional<ProductType> getByTitle(String title) {
        return this.productTypeRepository.findByTitle(title);
    }

}
