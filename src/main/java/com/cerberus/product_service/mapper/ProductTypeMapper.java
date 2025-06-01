package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.ProductTypeDto;
import com.cerberus.product_service.model.ProductType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductTypeMapper implements Mappable<ProductType, ProductTypeDto>{

    private final ModelMapper modelMapper;

    @Override
    public ProductType toEntity(ProductTypeDto productTypeDto) {
        return this.modelMapper.map(productTypeDto, ProductType.class);
    }

    @Override
    public ProductTypeDto toDto(ProductType productType) {
        return this.modelMapper.map(productType, ProductTypeDto.class);
    }

    @Override
    public List<ProductTypeDto> toDto(List<ProductType> e) {
        return e.stream()
                .map(productType -> this.modelMapper.map(productType, ProductTypeDto.class))
                .toList();
    }
}
