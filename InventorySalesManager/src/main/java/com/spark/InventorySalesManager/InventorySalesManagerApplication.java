package com.spark.InventorySalesManager;

import com.spark.InventorySalesManager.dto.ProductDto;
import com.spark.InventorySalesManager.dto.SaleDto;
import com.spark.InventorySalesManager.model.Role;
import com.spark.InventorySalesManager.model.User;
import com.spark.InventorySalesManager.repository.RoleRepository;
import com.spark.InventorySalesManager.repository.UserRepository;
import com.spark.InventorySalesManager.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class InventorySalesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventorySalesManagerApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ProductService productService, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (productService.getAllProducts(PageRequest.of(0, 1)).getTotalElements() == 0) {
                // Laptop with 2 sales
                ProductDto laptop = new ProductDto();
                laptop.setName("Laptop");
                laptop.setDescription("Gaming Laptop");
                laptop.setPrice(75000.0);
                laptop.setQuantity(10);

                SaleDto laptopSale1 = new SaleDto();
                laptopSale1.setQuantity(1);
                laptopSale1.setSaleDate(LocalDateTime.now().minusDays(2));

                SaleDto laptopSale2 = new SaleDto();
                laptopSale2.setQuantity(1);
                laptopSale2.setSaleDate(LocalDateTime.now());

                laptop.setSales(List.of(laptopSale1, laptopSale2));

                // Smartphone with 1 sale
                ProductDto smartphone = new ProductDto();
                smartphone.setName("Smartphone");
                smartphone.setDescription("Flagship Android");
                smartphone.setPrice(35000.0);
                smartphone.setQuantity(20);

                SaleDto smartphoneSale = new SaleDto();
                smartphoneSale.setQuantity(3);
                smartphoneSale.setSaleDate(LocalDateTime.now().minusDays(1));

                smartphone.setSales(List.of(smartphoneSale));

                // Monitor without sales
                ProductDto monitor = new ProductDto();
                monitor.setName("Monitor");
                monitor.setDescription("24-inch Monitor");
                monitor.setPrice(15000.0);
                monitor.setQuantity(15);
                monitor.setSales(List.of());

                // Save all products (and embedded sales)
                productService.addProduct(laptop);
                productService.addProduct(smartphone);
                productService.addProduct(monitor);
            }
			// Seed roles if not present
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

            // ✅ Admin User
            String adminUsername = "admin";
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);
                System.out.println("Admin user created");
            }

            // ✅ Normal User
            String userUsername = "user";
            if (userRepository.findByUsername(userUsername).isEmpty()) {
                User normalUser = new User();
                normalUser.setUsername(userUsername);
                normalUser.setPassword(passwordEncoder.encode("user123"));
                normalUser.setRoles(Set.of(userRole));
                userRepository.save(normalUser);
                System.out.println("Normal user created");
            }

        };
    }
}
