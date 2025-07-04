package com.spark.InventorySalesManager.repository;

import com.spark.InventorySalesManager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
