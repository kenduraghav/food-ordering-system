package com.foodorder.payment.service;

import static com.foodorder.payment.config.RabbitMQConfig.PAYMENT_EXCHANGE;
import static com.foodorder.payment.config.RabbitMQConfig.PAYMENT_PROCESSED_ROUTING_KEY;
import static com.foodorder.payment.config.RabbitMQConfig.PAYMENT_QUEUE;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.foodorder.payment.entity.Payment;
import com.foodorder.payment.entity.PaymentMethod;
import com.foodorder.payment.entity.PaymentStatus;
import com.foodorder.payment.event.OrderCreatedEvent;
import com.foodorder.payment.event.PaymentProcessedEvent;
import com.foodorder.payment.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;
	private final RabbitTemplate rabbitTemplate;
	private final PaymentGatewayService paymentGatewayService;

	public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate,
			PaymentGatewayService paymentGatewayService) {
		this.paymentRepository = paymentRepository;
		this.rabbitTemplate = rabbitTemplate;
		this.paymentGatewayService = paymentGatewayService;
	}

	@RabbitListener(queues = PAYMENT_QUEUE)
	@Transactional
	public void handleOrderCreated(OrderCreatedEvent event) {
		logger.info("Order Created Event: " + event.orderId());

		try {
			Payment payment = new Payment(event.orderId(), 
					event.userId(), 
					event.totalAmount(),
					PaymentMethod.valueOf(event.paymentMethod().toUpperCase()));
			
			payment.setStatus(PaymentStatus.PROCESSING);
			Payment savedPayment = paymentRepository.save(payment);
			
			//Process Payment through gateway (mock).
			PaymentGatewayResponse  gatewayResponse = paymentGatewayService.processPayment(
					savedPayment.getAmount(), 
					savedPayment.getPaymentMethod(), 
					savedPayment.getUserId());
			
			if(gatewayResponse.isSuccess()) {
				savedPayment.setStatus(PaymentStatus.SUCCESS);
				savedPayment.setTransactionId(gatewayResponse.getTransactionId());
				savedPayment.setProcessedAt(LocalDateTime.now());
			} else {
				savedPayment.setStatus(PaymentStatus.FAILED);
				savedPayment.setFailureReason(gatewayResponse.getErrorMessage());
			}
			
			savedPayment.setGatewayResponse(gatewayResponse.getRawResponse());
			paymentRepository.save(savedPayment);
			
			//Publish payment Process Event;
			// Publish payment processed event
            PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
                savedPayment.getId(),
                savedPayment.getOrderId(),
                savedPayment.getUserId(),
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                savedPayment.getTransactionId(),
                savedPayment.getFailureReason(),
                LocalDateTime.now()
            );
            
            rabbitTemplate.convertAndSend(
                PAYMENT_EXCHANGE,
                PAYMENT_PROCESSED_ROUTING_KEY,
                paymentEvent
            );
            
            logger.info("Payment processed and event published for order: " + event.orderId());
		} catch (Exception e) {
			logger.error("Error processing payment for order: " + event.orderId() + ", Error: " + e.getMessage());
            
            // Publish payment failed event
            PaymentProcessedEvent failedEvent = new PaymentProcessedEvent(
                UUID.randomUUID().toString(),
                event.orderId(),
                event.userId(),
                event.totalAmount(),
                PaymentStatus.FAILED,
                null,
                "Internal error: " + e.getMessage(),
                LocalDateTime.now()
            );
            
            rabbitTemplate.convertAndSend(
                PAYMENT_EXCHANGE,
                PAYMENT_PROCESSED_ROUTING_KEY,
                failedEvent
            );
		}
	}

}
