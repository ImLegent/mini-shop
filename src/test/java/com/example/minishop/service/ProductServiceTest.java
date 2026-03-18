package com.example.minishop.service;

import com.example.minishop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        productService.init(); // Pre-populates with 3 products
    }

    @Test
    void shouldAddProduct() {
        Product newProduct = new Product("99", "Test Product", new BigDecimal("12.99"));
        productService.addProduct(newProduct);
        
        Product fetchedProduct = productService.getProductById("99");
        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getName());
        assertEquals(new BigDecimal("12.99"), fetchedProduct.getPrice());
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertEquals(3, products.size());
        
        Product firstProduct = products.get(0);
        assertNotNull(firstProduct.getId());
        assertNotNull(firstProduct.getName());
    }
}
