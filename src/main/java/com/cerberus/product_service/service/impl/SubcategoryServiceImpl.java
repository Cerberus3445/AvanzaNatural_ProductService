package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.NotFoundException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.repository.SubcategoryRepository;
import com.cerberus.product_service.service.SubcategoryService;
import com.cerberus.product_service.util.CacheClear;
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
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    private final EntityDtoMapper mapper;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "subcategory", key = "#id")
    public SubcategoryDto get(Integer id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.subcategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategory",id)));
    }

    @Override
    @Cacheable(value = "subcategoryProducts", key = "#subcategoryId")
    public List<ProductDto> getSubcategoryProducts(Integer subcategoryId) {
        log.info("getSubcategoryProducts {}", subcategoryId);
        Subcategory subcategory = this.subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new NotFoundException("Subcategory",subcategoryId));
        return this.mapper.toDtoProductList(subcategory.getProducts());
    }

    @Override
    @Transactional
    public void create(SubcategoryDto subcategoryDto) {
        log.info("create {}", subcategoryDto);
        this.subcategoryRepository.save(this.mapper.toEntity(subcategoryDto));
    }

    @Override
    @CacheEvict(value = "subcategory", key = "#id")
    @Transactional
    public void update(Integer id, SubcategoryDto subcategoryDto) {
        log.info("update {}, {}", id, subcategoryDto);
        this.subcategoryRepository.findById(id).ifPresentOrElse(subcategory -> {
            Subcategory updatedSubcategory = Subcategory.builder()
                    .id(id)
                    .title(subcategoryDto.getTitle())
                    .products(subcategory.getProducts())
                    .build();
            this.subcategoryRepository.save(updatedSubcategory);
        }, () -> {
            throw new NotFoundException("Subcategory",id);
        });
    }

    @Override
    @CacheEvict(value = "subcategory", key = "#id")
    @Transactional
    public void delete(Integer id) {
        log.info("delete {}", id);
        SubcategoryDto subcategoryDto = get(id);
        this.subcategoryRepository.deleteById(id);
        this.cacheClear.clearSubcategoriesFromCategory(subcategoryDto.getCategoryId());
        this.cacheClear.clearProductsTypesFromSubcategory(id);
    }

    @Override
    public Optional<Subcategory> getByTitle(String title) {
        log.info("getByTitle {}", title);
        return this.subcategoryRepository.findByTitle(title);
    }

    @Override
    @Cacheable(value = "getProductsTypes", key = "#id")
    public List<ProductTypeDto> getProductsTypes(Integer id) {
        log.info("getProductsTypes {}", id);
        Subcategory subcategory = this.subcategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategory",id));
        return this.mapper.toDtoProductTypeList(subcategory.getProductTypes());
    }
}
