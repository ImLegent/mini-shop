package com.example.minishop.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    @Test
    public void shouldCreateOrderUsingAllArgsConstructor() {
        // Arrange
        String expectedId = "order-123";
        List<String> expectedProductIds = Arrays.asList("prod-1", "prod-2");
        BigDecimal expectedTotalAmount = new BigDecimal("99.99");
        String expectedStatus = "CREATED";

        // Act
        Order order = new Order(expectedId, expectedProductIds, expectedTotalAmount, expectedStatus);

        // Assert
        assertEquals(expectedId, order.getId(), "Order ID should match");
        assertEquals(expectedProductIds, order.getProductIds(), "Product IDs should match");
        assertEquals(expectedTotalAmount, order.getTotalAmount(), "Total amount should match");
        assertEquals(expectedStatus, order.getStatus(), "Status should match");
    }
}
