package com.cerberus.product_service.mapper;

import com.cerberus.product_service.dto.CategoryDto;
import com.cerberus.product_service.dto.ProductDto;
import com.cerberus.product_service.dto.SubcategoryDto;
import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.Product;
import com.cerberus.product_service.model.Subcategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityDtoMapper {

    @Autowired
    private ModelMapper mapper;

    public Product toEntity(ProductDto productDto){
        return this.mapper.map(productDto, Product.class);
    }

    public ProductDto toDto(Product product){
        return this.mapper.map(product, ProductDto.class);
    }

    public List<ProductDto> toDto(List<Product> products){
        return products.stream().map(this::toDto).toList();
    }

    public Category toEntity(CategoryDto categoryDto){
        return this.mapper.map(categoryDto, Category.class);
    }

    public CategoryDto toDto(Category category){
        return this.mapper.map(category, CategoryDto.class);
    }

    public Subcategory toEntity(SubcategoryDto subcategoryDto){
        return this.mapper.map(subcategoryDto, Subcategory.class);
    }

    public SubcategoryDto toDto(Subcategory subcategory){
        return this.mapper.map(subcategory, SubcategoryDto.class);
    }
}
