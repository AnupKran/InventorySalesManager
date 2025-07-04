package com.spark.InventorySalesManager.exception;

public enum ProductErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT-001", "Product with given ID not found"),
    PRODUCT_SAVE_FAILED("PRODUCT-002", "Failed to save product"),
    PRODUCT_UPDATE_FAILED("PRODUCT-003", "Failed to update product"),
    PRODUCT_DELETE_FAILED("PRODUCT-004", "Failed to delete product"),
    PRODUCT_PDF_EMPTY("PRODUCT-005", "No products found to generate PDF");

    private final String code;
    private final String message;

    ProductErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
