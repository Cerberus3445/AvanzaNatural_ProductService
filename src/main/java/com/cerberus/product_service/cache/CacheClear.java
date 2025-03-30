package com.cerberus.product_service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheClear {

    @CacheEvict(value = "getSubcategoriesOfCertainCategory", key = "#categoryId")
    public void clearSubcategoriesOfCertainCategory(Integer categoryId){
        log.info("clearSubcategoriesOfCertainCategory {}", categoryId);
    }

    @CacheEvict(value = "getProductsTypes", key = "#subcategoryId")
    public void clearProductsTypesOfCertainSubcategory(Integer subcategoryId){
        log.info("clearProductsTypesOfCertainSubcategory {}", subcategoryId);
    }

    @CacheEvict(value = "getAllCategories")
    public void clearAllCategories(){
        log.info("clearCategories");
    }

    @CacheEvict(value = "paginationOfProductsByCategory", key = "#categoryId")
    public void clearPaginationOfProductsByCategory(Integer categoryId){
        log.info("clearPaginationOfProductsByCategory {}", categoryId);
    }

    @CacheEvict(value = "paginationOfProductsBySubcategory", key = "#subcategoryId")
    public void clearPaginationOfProductsBySubcategory(Integer subcategoryId){
        log.info("clearPaginationOfProductsBySubcategory {}", subcategoryId);
    }

    @CacheEvict(value = "paginationOfProductsByProductType", key = "#productTypeId")
    public void clearPaginationOfProductsByProductType(Integer productTypeId){
        log.info("clearPaginationOfProductsByProductType {}", productTypeId);
    }
}
