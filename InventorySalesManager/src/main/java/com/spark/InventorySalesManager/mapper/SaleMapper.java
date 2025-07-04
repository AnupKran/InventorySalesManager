package com.spark.InventorySalesManager.mapper;

import com.spark.InventorySalesManager.dto.SaleDto;
import com.spark.InventorySalesManager.model.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    SaleDto toDto(Sale sale);
    Sale toEntity(SaleDto dto);
}
