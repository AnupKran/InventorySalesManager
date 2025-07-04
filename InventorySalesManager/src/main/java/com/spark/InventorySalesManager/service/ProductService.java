package com.spark.InventorySalesManager.service;

import com.spark.InventorySalesManager.dto.PagedProductResponse;
import com.spark.InventorySalesManager.dto.ProductDto;
import com.spark.InventorySalesManager.dto.ProductResponseDto;
import com.spark.InventorySalesManager.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    PagedProductResponse<ProductDto> getAllProducts(Pageable pageable);

    ProductDto getProductById(Long id);

    ProductResponseDto addProduct(ProductDto product);

    boolean updateProduct(Long id, ProductDto product);

    boolean deleteProduct(Long id);

    double getTotalRevenue();

    double getRevenueByProduct(Long productId);

    byte[] generateProductsPdf();
}
