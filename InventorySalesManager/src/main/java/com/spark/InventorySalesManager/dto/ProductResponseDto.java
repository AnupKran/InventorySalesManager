package com.spark.InventorySalesManager.dto;

import lombok.Data;

import java.util.List;
@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private List<SaleDto> sales;
}
