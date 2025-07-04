package com.spark.InventorySalesManager.controller;

import com.lowagie.text.DocumentException;
import com.spark.InventorySalesManager.dto.PagedProductResponse;
import com.spark.InventorySalesManager.dto.ProductDto;
import com.spark.InventorySalesManager.dto.ProductResponseDto;
import com.spark.InventorySalesManager.model.Product;
import com.spark.InventorySalesManager.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;


    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all products", tags = {"Product"})
    public ResponseEntity<PagedProductResponse<ProductDto>> getAllProducts(Pageable pageable) {
        PagedProductResponse<ProductDto> response = productService.getAllProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "get a product", tags = { "Product"})
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a product", tags = { "Product" })
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductDto product) {
        ProductResponseDto responseDto = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product", tags = {"Product"})
    public ResponseEntity<Boolean> updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        boolean updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a product", tags = {"Product" })
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all product revenue", tags = {"Sales" })
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(productService.getTotalRevenue());
    }

    @GetMapping("/{id}/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Revenue of a product", tags = { "Sales" })
    public ResponseEntity<Double> getRevenueByProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRevenueByProduct(id));
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "download all product", tags = { "Product" })
    public ResponseEntity<byte[]> downloadPdf() throws DocumentException {
        byte[] pdfBytes  = productService.generateProductsPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
