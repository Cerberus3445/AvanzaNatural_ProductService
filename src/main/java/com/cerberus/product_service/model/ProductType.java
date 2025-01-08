package com.cerberus.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Table(name = "Products_Types")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "productType")
    private List<Product> products;
}
