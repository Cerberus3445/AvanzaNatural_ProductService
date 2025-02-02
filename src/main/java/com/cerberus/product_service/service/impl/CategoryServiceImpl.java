package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.repository.CategoryRepository;
import com.cerberus.product_service.service.CategoryService;
import com.cerberus.product_service.service.StorageService;
import com.cerberus.product_service.util.CacheClear;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EntityDtoMapper mapper;

    private final StorageService storageService;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "category", key = "#id")
    public CategoryDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category",id)));
    }

    @Override
    @Cacheable(value = "getAllCategories")
    public List<CategoryDto> getAll() {
        return this.mapper.toDtoCategoryList(this.categoryRepository.findAll());
    }

    @Override
    @Cacheable(value = "categoryProducts", key = "#id")
    public List<ProductDto> getCategoryProducts(Integer id) {
        log.info("getCategoryProducts {}", id);
         Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category",id));
         return this.mapper.toDtoProductList(category.getProducts());
    }

    @Override
    @Transactional
    public void create(CategoryDto categoryDto) {
        log.info("create {}", categoryDto);
        this.categoryRepository.save(this.mapper.toEntity(categoryDto));
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
        return this.mapper.toDtoSubcategoryList(category.getSubcategories());
    }
}
