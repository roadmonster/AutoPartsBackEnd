package com.hao.paymentservice.repository;

import com.hao.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByOrderUniqueId(String orderUniqueId);
}
