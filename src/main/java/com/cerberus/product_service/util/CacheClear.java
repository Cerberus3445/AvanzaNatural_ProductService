package com.cerberus.product_service.util;

import com.cerberus.product_service.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheClear {

    @CacheEvict(value = "categoryProducts", key = "#id")
    public void clearCategoryCache(Integer id){
        log.info("clearCategoryCache {}", id);
    }

    @CacheEvict(value = "subcategoryProducts", key = "#id")
    public void clearSubcategoryCache(Integer id){
        log.info("clearSubcategoryCache {}", id);
    }

    @CacheEvict(value = "productTypeProducts", key = "#id")
    public void clearProductTypeCache(Integer id){
        log.info("clearProductTypeCache {}", id);
    }

    /*
    don't work:
    public void clearCache(ProductDto productDto){
        clearCategoryCache(productDto.getCategoryId());
        clearSubcategoryCache(productDto.getSubcategoryId());
        clearProductTypeCache(productDto.getProductTypeId());
    }
     */
}
