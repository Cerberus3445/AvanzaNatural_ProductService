package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.model.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper extends Mappable<ProductType, ProductTypeDto>{

    @Override
    @Mapping(target = "subcategory", source = "subcategoryId")
    ProductType toEntity(ProductTypeDto productTypeDto);

    @Override
    @Mapping(target = "subcategoryId", source = "subcategory")
    ProductTypeDto toDto(ProductType productType);

    default List<ProductTypeDto> toDto(List<ProductType> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)
                .toList();
    }

}
