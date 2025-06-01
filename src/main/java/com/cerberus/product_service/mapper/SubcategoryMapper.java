package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Subcategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubcategoryMapper implements Mappable<Subcategory, SubcategoryDto>{

    private final ModelMapper modelMapper;

    @Override
    public Subcategory toEntity(SubcategoryDto subcategoryDto) {
        return this.modelMapper.map(subcategoryDto, Subcategory.class);
    }

    @Override
    public SubcategoryDto toDto(Subcategory subcategory) {
        return this.modelMapper.map(subcategory, SubcategoryDto.class);
    }

    @Override
    public List<SubcategoryDto> toDto(List<Subcategory> e) {
        return e.stream()
                .map(subcategory -> this.modelMapper.map(subcategory, SubcategoryDto.class))
                .toList();
    }
}
