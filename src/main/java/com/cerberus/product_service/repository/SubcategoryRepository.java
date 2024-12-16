package com.cerberus.product_service.repository;

import com.cerberus.product_service.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer>{

    Optional<Subcategory> findByTitle(String title);
}
