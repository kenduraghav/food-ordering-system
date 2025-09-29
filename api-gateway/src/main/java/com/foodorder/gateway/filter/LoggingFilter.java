package com.foodorder.gateway.filter;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Add correlation ID for tracing
        String correlationId = UUID.randomUUID().toString();
        ServerHttpRequest mutatedRequest = request.mutate()
            .header("X-Correlation-ID", correlationId)
            .build();
        
        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build();
        
        // Log request
        System.out.printf("[%s] %s %s - Correlation-ID: %s%n",
            LocalDateTime.now(),
            request.getMethod(),
            request.getPath(),
            correlationId
        );
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(mutatedExchange)
            .doFinally(signalType -> {
                long endTime = System.currentTimeMillis();
                System.out.printf("[%s] Response time: %dms - Correlation-ID: %s%n",
                    LocalDateTime.now(),
                    endTime - startTime,
                    correlationId
                );
            });
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}