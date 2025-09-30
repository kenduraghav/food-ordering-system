package com.foodorder.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        String correlationId = UUID.randomUUID().toString();
        
        // Add correlation ID to request
        var mutatedRequest = request.mutate()
            .header("X-Correlation-ID", correlationId)
            .build();
        
        var mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build();
        
        long startTime = System.currentTimeMillis();
        
        System.out.printf("[%s] [%s] %s %s - Routing started%n",
            LocalDateTime.now(),
            correlationId,
            request.getMethod(),
            request.getPath()
        );
        
        return chain.filter(mutatedExchange)
            .doOnSuccess(aVoid -> {
                long duration = System.currentTimeMillis() - startTime;
                var response = exchange.getResponse();
                HttpStatus statusCode = (HttpStatus) response.getStatusCode();
                
                System.out.printf("[%s] [%s] %s %s - Completed: %s (%dms)%n",
                    LocalDateTime.now(),
                    correlationId,
                    request.getMethod(),
                    request.getPath(),
                    statusCode,
                    duration
                );
            })
            .doOnError(error -> {
                long duration = System.currentTimeMillis() - startTime;
                
                System.err.printf("[%s] [%s] %s %s - Error: %s (%dms)%n",
                    LocalDateTime.now(),
                    correlationId,
                    request.getMethod(),
                    request.getPath(),
                    error.getMessage(),
                    duration
                );
            });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}