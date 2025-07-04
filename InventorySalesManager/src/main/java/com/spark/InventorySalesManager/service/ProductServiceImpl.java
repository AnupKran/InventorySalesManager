package com.spark.InventorySalesManager.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.spark.InventorySalesManager.dto.PagedProductResponse;
import com.spark.InventorySalesManager.dto.ProductDto;
import com.spark.InventorySalesManager.dto.ProductResponseDto;
import com.spark.InventorySalesManager.exception.ProductErrorCode;
import com.spark.InventorySalesManager.exception.ProductServiceCustomException;
import com.spark.InventorySalesManager.mapper.ProductMapper;
import com.spark.InventorySalesManager.model.Product;
import com.spark.InventorySalesManager.model.Sale;
import com.spark.InventorySalesManager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public PagedProductResponse<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDto> productDtos = productPage
                .map(productMapper::toDto)
                .getContent();
        return new PagedProductResponse<>(
                productDtos,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize()
        );
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductServiceCustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return  productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto addProduct(ProductDto product) {
        try {
            // Map DTO to entity
            Product productData = productMapper.toEntity(product);

            if (product.getSales() != null) {
                for (Sale sale : productData.getSales()) {
                    sale.setProduct(productData);
                }
            }
            Product savedProduct = productRepository.save(productData);
            return productMapper.toResponseDto(savedProduct);
        } catch (Exception e) {
            throw new ProductServiceCustomException(ProductErrorCode.PRODUCT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public boolean updateProduct(Long id, ProductDto product) {
        if (!productRepository.existsById(id)) {
            throw new ProductServiceCustomException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }
        try {
            Product productData = productMapper.toEntity(product);
            productData.setId(id);
            if (product.getSales() != null) {
                for (Sale sale : productData.getSales()) {
                    sale.setProduct(productData);
                }
            }
            productRepository.save(productData);
            return true;
        } catch (Exception e) {
            throw new ProductServiceCustomException(ProductErrorCode.PRODUCT_UPDATE_FAILED);
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductServiceCustomException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new ProductServiceCustomException(ProductErrorCode.PRODUCT_DELETE_FAILED);
        }
    }

    @Override
    public double getTotalRevenue() {
        List<Product> products = productRepository.findAll();
        double totalRevenue = 0.0;
        for (Product product : products) {
            for (Sale sale : product.getSales()) {
                totalRevenue += sale.getQuantity() * product.getPrice();
            }
        }
        return totalRevenue;
    }

    @Override
    public double getRevenueByProduct(Long productId) {
        ProductDto product = getProductById(productId);
        if (product == null) return 0.0;
        return product.getSales().stream()
                .mapToDouble(sale -> sale.getQuantity() * product.getPrice())
                .sum();
    }

    @Override
    public byte[] generateProductsPdf() {
        List<Product> product = productRepository.findAll();
        if (CollectionUtils.isEmpty(product)) {
            return new byte[0];
        }
       return generateProductsPdf(product);
    }

    public byte[] generateProductsPdf(List<Product> products) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Product Table", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Create table with 6 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 3f, 4f, 2f, 2f, 2f});

        // Add header cells
        Stream.of("ID", "Name", "Description", "Price", "Quantity", "Revenue").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setPhrase(new Phrase(headerTitle));
            table.addCell(header);
        });

        // Add product data rows
        for (Product product : products) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getDescription());
            table.addCell(String.format("%.2f", product.getPrice()));
            table.addCell(String.valueOf(product.getQuantity()));
            double revenue = getRevenueByProduct(product.getId());
            table.addCell(String.format("%.2f", revenue));
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
