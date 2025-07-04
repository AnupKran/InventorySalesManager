package com.spark.InventorySalesManager.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SaleDto {
    private Integer id;
    private Integer quantity;
    private LocalDateTime saleDate;
}
