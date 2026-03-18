package com.example.minishop.util;

import java.math.BigDecimal;

public class DiscountCalculator {

    /**
     * Calculates the final price after applying a percentage discount.
     */
    public BigDecimal calculateDiscount(BigDecimal price, double percentage) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        
        if (percentage <= 0 || percentage >= 100) {
            return price;
        }
        
        BigDecimal discountAmount = price.multiply(BigDecimal.valueOf(percentage / 100.0));
        return price.subtract(discountAmount);
    }
    
    /**
     * Determines if an order is eligible for free shipping based on the final price.
     */
    public boolean isEligibleForFreeShipping(BigDecimal finalPrice) {
        return finalPrice != null && finalPrice.compareTo(new BigDecimal("50.00")) >= 0;
    }
}
