package com.cerberus.product_service.repository;

import com.cerberus.product_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    Optional<Category> findByTitle(String title);
}
