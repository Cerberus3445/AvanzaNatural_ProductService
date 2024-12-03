package com.cerberus.product_service.service.impl;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.exception.CategoryException;
import com.cerberus.product_service.exception.SubcategoryException;
import com.cerberus.product_service.mapper.EntityDtoMapper;
import com.cerberus.product_service.model.Subcategory;
import com.cerberus.product_service.repository.SubcategoryRepository;
import com.cerberus.product_service.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SubcategoryServiceImpl implements SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "subcategory", key = "#id")
    public SubcategoryDto get(Integer id) {
        return this.mapper.toDto(this.subcategoryRepository.findById(id)
                .orElseThrow(() -> new SubcategoryException("Subcategory with %d id not found.".formatted(id))));
    }

    @Override
    @Cacheable(value = "subcategoryProducts", key = "#id")
    public List<ProductDto> getSubcategoryProducts(Integer subcategoryId) {
        Subcategory subcategory = this.subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new SubcategoryException("Subcategory with %d id not found.".formatted(subcategoryId)));
        return this.mapper.toDto(subcategory.getProducts());
    }

    @Override
    @Transactional
    public void create(SubcategoryDto subcategoryDto) {
        this.subcategoryRepository.save(this.mapper.toEntity(subcategoryDto));
    }

    @Override
    @CacheEvict(value = "subcategory", key = "#id")
    @Transactional
    public void update(Integer id, SubcategoryDto subcategoryDto) {
        this.subcategoryRepository.findById(id).ifPresentOrElse(subcategory -> {
            Subcategory updatedSubcategory = Subcategory.builder()
                    .id(id)
                    .title(subcategoryDto.getTitle())
                    .products(subcategory.getProducts())
                    .build();
            this.subcategoryRepository.save(updatedSubcategory);
        }, () -> {
            throw new CategoryException("Subcategory with %d id not found.".formatted(id));
        });
    }

    @Override
    @CacheEvict(value = "subcategory", key = "#id")
    @Transactional
    public void delete(Integer id) {
        this.subcategoryRepository.deleteById(id);
    }
}
