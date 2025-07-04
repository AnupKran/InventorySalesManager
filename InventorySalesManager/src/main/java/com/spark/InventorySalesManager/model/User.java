package com.spark.InventorySalesManager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
