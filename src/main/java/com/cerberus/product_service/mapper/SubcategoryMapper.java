package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Subcategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper extends Mappable<Subcategory, SubcategoryDto>{

    @Override
    @Mapping(target = "category", source = "categoryId")
    Subcategory toEntity(SubcategoryDto subcategoryDto);

    @Override
    @Mapping(target = "categoryId", source = "category")
    SubcategoryDto toDto(Subcategory subcategory);

    default List<SubcategoryDto> toDto(List<Subcategory> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)
                .toList();
    }
}
