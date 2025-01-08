package com.cerberus.product_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheClear {

    @CacheEvict(value = "categoryProducts", key = "#categoryId")
    public void clearCategoryProducts(Integer categoryId){
        log.info("clearCategoryProducts {}", categoryId);
    }

    @CacheEvict(value = "subcategoryProducts", key = "#subcategoryId")
    public void clearSubcategoryProducts(Integer subcategoryId){
        log.info("clearSubcategoryProducts {}", subcategoryId);
    }

    @CacheEvict(value = "productTypeProducts", key = "#productTypeId")
    public void clearProductTypeProducts(Integer productTypeId){
        log.info("clearProductTypeProducts {}", productTypeId);
    }

    @CacheEvict(value = "getProductsTypes", key = "#subcategoryId")
    public void clearProductsTypesFromSubcategory(Integer subcategoryId){
        log.info("clearProductsTypesFromSubcategory {}", subcategoryId);
    }

    @CacheEvict(value = "getSubcategories", key = "#categoryId")
    public void clearSubcategoriesFromCategory(Integer categoryId){
        log.info("clearSubcategoriesFromCategory {}", categoryId);
    }
}
