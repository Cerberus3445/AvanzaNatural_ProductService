package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.repository.ProductTypeRepository;
import com.cerberus.product_service.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    private final EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "productType", key = "#id")
    public ProductTypeDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType",id)));
    }

    @Override
    @Cacheable(value = "productTypeProducts", key = "#id")
    public List<ProductDto> getProductTypeProducts(Integer id) {
        log.info("getProductTypeProducts {}", id);
        ProductType productType =  this.productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType",id));
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
            throw new NotFoundException("ProductType",id);
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
