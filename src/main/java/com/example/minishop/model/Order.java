package com.example.minishop.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private String id;
    private List<String> productIds;
    private BigDecimal totalAmount;
    private String status;

    public Order() {
    }

    public Order(String id, List<String> productIds, BigDecimal totalAmount, String status) {
        this.id = id;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
