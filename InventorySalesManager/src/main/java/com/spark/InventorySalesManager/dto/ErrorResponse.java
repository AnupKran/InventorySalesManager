package com.spark.InventorySalesManager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String errorMessage;
    private String errorCode;
    private String path;
}
