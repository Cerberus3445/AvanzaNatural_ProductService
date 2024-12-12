package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.CategoryException;
import com.cerberus.product_service.exception.ProductException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.repository.ProductRepository;
import com.cerberus.product_service.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.productRepository.findById(id)
                .orElseThrow(() -> new ProductException("404. Product with %d id not found.".formatted(id))));
    }

    @Override
    @Transactional
    public void create(ProductDto productDto) {
        log.info("create {}", productDto);
        this.productRepository.save(this.mapper.toEntity(productDto));
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    @Transactional
    public void update(Integer id, ProductDto productDto) {
        log.info("update {}, {}", id, productDto);
        this.productRepository.findById(id).ifPresentOrElse(product -> {
            Product updatedProduct = Product.builder()
                    .id(id)
                    .title(productDto.getTitle())
                    .description(productDto.getDescription())
                    .price(productDto.getPrice())
                    .inStock(productDto.getInStock())
                    .category(product.getCategory())
                    .subcategory(product.getSubcategory())
                    .productType(product.getProductType())
                    .build();
            this.productRepository.save(updatedProduct);
        }, () -> {
            throw new CategoryException("404. Product with %d id not found.".formatted(id));
        });
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        this.productRepository.deleteById(id);
    }
}
