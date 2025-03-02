package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.ProductMapper;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.repository.ProductRepository;
import com.cerberus.product_service.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductDto get(Integer id) {
        log.info("get {}", id);
        return this.productMapper.toDto(this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product",id)));
    }

    @Override
    @Transactional
    public void create(ProductDto productDto) {
        log.info("create {}", productDto);
        this.productRepository.save(this.productMapper.toEntity(productDto));
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
                    .brand(productDto.getBrand())
                    .description(productDto.getDescription())
                    .price(productDto.getPrice())
                    .inStock(productDto.getInStock())
                    .category(product.getCategory())
                    .subcategory(product.getSubcategory())
                    .productType(product.getProductType())
                    .build();
            this.productRepository.save(updatedProduct);
        }, () -> {
            throw new NotFoundException("Product",id);
        });
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        this.productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getByTitle(String title) {
        return this.productRepository.findByTitle(title);
    }
}
