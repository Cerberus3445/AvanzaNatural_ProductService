package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.exception.CategoryException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.repository.CategoryRepository;
import com.cerberus.product_service.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "category", key = "#id")
    public CategoryDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("404.Category with %d id not found.".formatted(id))));
    }

    @Override
    @Cacheable(value = "categoryProducts", key = "#id")
    public List<ProductDto> getCategoryProducts(Integer id) {
        log.info("getCategoryProducts {}", id);
         Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("404.Category with %d id not found.".formatted(id)));
         return this.mapper.toDto(category.getProducts());
    }

    @Override
    @Transactional
    public void create(CategoryDto categoryDto) {
        log.info("create {}", categoryDto);
        this.categoryRepository.save(this.mapper.toEntity(categoryDto));
    }

    @Override
    @CacheEvict(value = "category", key = "#id")
    @Transactional
    public void update(Integer id, CategoryDto categoryDto) {
        log.info("update {}, {}", id, categoryDto);
        this.categoryRepository.findById(id).ifPresentOrElse(category -> {
            Category updateCategory = Category.builder()
                    .id(id)
                    .title(categoryDto.getTitle())
                    .products(category.getProducts())
                    .build();
            this.categoryRepository.save(updateCategory);
        }, () -> {
            throw new CategoryException("404.Category with %d id not found.".formatted(id));
        });
    }

    @Override
    @CacheEvict(value = "category", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        this.categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getByTitle(String title) {
        return this.categoryRepository.findByTitle(title);
    }
}
