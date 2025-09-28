package com.foodorder.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodorder.payment.entity.Payment;
import com.foodorder.payment.entity.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

	Optional<Payment> findByOrderId(String orderId);

	List<Payment> findByUserId(String userId);

	List<Payment> findByStatus(PaymentStatus status);

	List<Payment> findByUserIdAndStatus(String userId, PaymentStatus status);
}
