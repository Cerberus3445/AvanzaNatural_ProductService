package com.cerberus.product_service.mapper;

import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.ProductType;
import com.cerberus.product_service.model.Subcategory;

import java.util.List;

public interface Mappable<E, D>{

    E toEntity(D d);

    D toDto(E e);

    List<D> toDto(List<E> e);

    default Category mapCategoryIdToCategory(Integer categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default Subcategory mapSubcategoryIdToSubcategory(Integer subcategoryId) {
        if (subcategoryId == null) {
            return null;
        }
        Subcategory subcategory = new Subcategory();
        subcategory.setId(subcategoryId);
        return subcategory;
    }

    default ProductType mapProductTypeIdToProductType(Integer productTypeId) {
        if (productTypeId == null) {
            return null;
        }
        ProductType productType = new ProductType();
        productType.setId(productTypeId);
        return productType;
    }

    default Integer mapCategoryToCategoryId(Category category) {
        if (category == null) {
            return null;
        }
        return category.getId();
    }

    default Integer mapSubcategorySubcategoryId(Subcategory subcategory) {
        if (subcategory == null) {
            return null;
        }
        return subcategory.getId();
    }

    default Integer mapProductTypeToProductTypeId(ProductType productType) {
        if (productType == null) {
            return null;
        }
        return productType.getId();
    }
}
