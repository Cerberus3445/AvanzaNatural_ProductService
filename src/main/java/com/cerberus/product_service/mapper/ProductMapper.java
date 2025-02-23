package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper extends Mappable<Product, ProductDto>{

    @Override
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "subcategory", source = "subcategoryId")
    @Mapping(target = "productType", source = "productTypeId")
    Product toEntity(ProductDto productDto);

    @Override
    @Mapping(target = "categoryId", source = "category")
    @Mapping(target = "subcategoryId", source = "subcategory")
    @Mapping(target = "productTypeId", source = "productType")
    ProductDto toDto(Product product);

    default List<ProductDto> toDto(List<Product> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)
                .toList();
    }
}
