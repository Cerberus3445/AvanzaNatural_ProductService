package com.cerberus.product_service.repository;

import com.cerberus.product_service.model.Category;
import com.cerberus.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
