package com.example.minishop.service;

import com.example.minishop.model.Order;
import com.example.minishop.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final ProductService productService;

    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    public Order createOrder(List<String> productIds) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (String productId : productIds) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                totalAmount = totalAmount.add(product.getPrice());
            } 
            // Note: In a real app we might handle invalid IDs here, 
            // but for a minimal demo we might ignore or keep it simple.
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setProductIds(productIds);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");

        orders.put(order.getId(), order);
        return order;
    }
    
    public Order getOrder(String id) {
        return orders.get(id);
    }
    
    public List<Order> getAllOrders() {
        return orders.values().stream().collect(Collectors.toList());
    }
}
