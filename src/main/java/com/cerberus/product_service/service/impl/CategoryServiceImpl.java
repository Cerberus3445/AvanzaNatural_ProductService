package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.cache.CacheClear;
import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.CategoryMapper;
import com.cerberus.product_service.mapper.SubcategoryMapper;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.repository.CategoryRepository;
import com.cerberus.product_service.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final SubcategoryMapper subcategoryMapper;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "category", key = "#id")
    public CategoryDto get(Integer id) {
        log.info("get {}", id);
        return this.categoryMapper.toDto(this.categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category",id)));
    }

    @Override
    @Cacheable(value = "getAllCategories")
    public List<CategoryDto> getAll() {
        return this.categoryMapper.toDto(this.categoryRepository.findAll());
    }

    @Override
    @Transactional
    public void create(CategoryDto categoryDto) {
        log.info("create {}", categoryDto);
        this.categoryRepository.save(this.categoryMapper.toEntity(categoryDto));

        this.cacheClear.clearAllCategories();
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
            throw new NotFoundException("Category",id);
        });

        this.cacheClear.clearAllCategories();
    }

    @Override
    @CacheEvict(value = "category", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        this.categoryRepository.deleteById(id);

        this.cacheClear.clearAllCategories();
    }

    @Override
    public Optional<Category> getByTitle(String title) {
        return this.categoryRepository.findByTitle(title);
    }

    @Override
    @Cacheable(value = "getSubcategoriesOfCertainCategory", key = "#id")
    public List<SubcategoryDto> getSubcategories(Integer id) {
        log.info("getSubcategories {}", id);
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category",id));
        return this.subcategoryMapper.toDto(category.getSubcategories());
    }
}
