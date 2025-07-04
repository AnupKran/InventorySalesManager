package com.spark.InventorySalesManager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price",nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Sale> sales = new ArrayList<>();
}
