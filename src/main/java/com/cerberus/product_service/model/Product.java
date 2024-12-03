package com.cerberus.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "Products")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Double price;

    private Boolean inStock;

    @ManyToOne
    private Category category;

    @ManyToOne
    private ProductType productType;

    @ManyToOne
    private Subcategory subcategory;
}
