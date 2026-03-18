package com.example.minishop.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductTest {

    @Test
    public void shouldCreateProductUsingNoArgsConstructorAndSetters() {
        // Arrange
        Product product = new Product();
        String expectedId = "prod-1";
        String expectedName = "Laptop";
        BigDecimal expectedPrice = new BigDecimal("999.99");

        // Act
        product.setId(expectedId);
        product.setName(expectedName);
        product.setPrice(expectedPrice);

        // Assert
        assertEquals(expectedId, product.getId());
        assertEquals(expectedName, product.getName());
        assertEquals(expectedPrice, product.getPrice());
    }

    @Test
    public void shouldCreateProductUsingAllArgsConstructor() {
        // Arrange
        String expectedId = "prod-2";
        String expectedName = "Smartphone";
        BigDecimal expectedPrice = new BigDecimal("499.50");

        // Act
        Product product = new Product(expectedId, expectedName, expectedPrice);

        // Assert
        assertEquals(expectedId, product.getId());
        assertEquals(expectedName, product.getName());
        assertEquals(expectedPrice, product.getPrice());
    }
}
