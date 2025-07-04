package com.spark.InventorySalesManager.exception;

import com.spark.InventorySalesManager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(
            ProductServiceCustomException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .errorMessage(ex.getErrorCode().getMessage())
                .errorCode(ex.getErrorCode().getCode())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Optional fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(ex.getMessage())
                .errorCode("GENERIC_ERROR")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
