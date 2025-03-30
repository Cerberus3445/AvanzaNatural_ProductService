package com.cerberus.product_service.repository;

import com.cerberus.product_service.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    Optional<Product> findByTitle(String title);

    List<Product> findByCategory_Id(Integer categoryId, Pageable pageable);

    List<Product> findBySubcategory_Id(Integer categoryId, Pageable pageable);

    List<Product> findByProductType_Id(Integer categoryId, Pageable pageable);
}
