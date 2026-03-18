package com.example.minishop.controller;

import com.example.minishop.model.Order;
import com.example.minishop.model.Product;
import com.example.minishop.service.OrderService;
import com.example.minishop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShopController {

    private final ProductService productService;
    private final OrderService orderService;

    public ShopController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody List<String> productIds) {
        Order order = orderService.createOrder(productIds);
        return ResponseEntity.ok(order);
    }

    // Code Smell: Duplicate of getProductById
    @GetMapping("/products/v2/{id}")
    public ResponseEntity<Product> getProductByIdV2(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Code Smell: Duplicate of createOrder
    @PostMapping("/orders/v2")
    public ResponseEntity<Order> createOrderV2(@RequestBody List<String> productIds) {
        Order order = orderService.createOrder(productIds);
        return ResponseEntity.ok(order);
    }
}
