package com.spark.InventorySalesManager.exception;

import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductServiceCustomException(ProductErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
