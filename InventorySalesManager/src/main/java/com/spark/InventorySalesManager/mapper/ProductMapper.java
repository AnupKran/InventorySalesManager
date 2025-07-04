package com.spark.InventorySalesManager.mapper;

import com.spark.InventorySalesManager.dto.ProductDto;
import com.spark.InventorySalesManager.dto.ProductResponseDto;
import com.spark.InventorySalesManager.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);

    Product toEntity(ProductDto dto);

    ProductResponseDto toResponseDto(Product product);
}
