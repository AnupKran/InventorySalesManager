package com.spark.InventorySalesManager.repository;

import com.spark.InventorySalesManager.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
