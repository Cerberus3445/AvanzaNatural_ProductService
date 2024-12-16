package com.cerberus.product_service.repository;

import com.cerberus.product_service.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer>{

    Optional<ProductType> findByTitle(String title);
}
