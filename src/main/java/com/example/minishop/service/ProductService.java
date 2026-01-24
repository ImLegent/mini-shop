package com.example.minishop.service;

import com.example.minishop.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductService {

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Pre-populate with some dummy data
        addProduct(new Product("1", "Laptop", new BigDecimal("999.99")));
        addProduct(new Product("2", "Smartphone", new BigDecimal("499.99")));
        addProduct(new Product("3", "Headphones", new BigDecimal("49.99")));
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(String id) {
        return products.get(id);
    }
}
