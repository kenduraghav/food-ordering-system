package com.foodorder.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.foodorder.payment.entity.PaymentMethod;

@Service
public class PaymentGatewayService {

	public PaymentGatewayResponse processPayment(BigDecimal amount, PaymentMethod method, String userId) {
        // Simulate payment processing delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Mock success rate (90% success, 10% failure)
        boolean isSuccess = Math.random() > 0.1;
        
        if (isSuccess) {
            return PaymentGatewayResponse.success(
                UUID.randomUUID().toString(),
                "Payment processed successfully",
                "{\"status\":\"success\",\"gateway\":\"mock\"}"
            );
        } else {
            return PaymentGatewayResponse.failure(
                "Insufficient funds",
                "{\"status\":\"failed\",\"error\":\"insufficient_funds\"}"
            );
        }
    }

}
