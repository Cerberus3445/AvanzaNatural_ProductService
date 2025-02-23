package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Mappable<Category, CategoryDto>{

    default List<CategoryDto> toDto(List<Category> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)
                .toList();
    }
}
