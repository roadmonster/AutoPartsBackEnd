package com.hao.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.paymentservice.entity.Payment;
import com.hao.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    private void setPaymentRepository(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);
    public Payment executePayment(Payment payment){
        boolean isProcessed = paymentProcessing();
        if (isProcessed){
            payment.setOrderStatus("Processed");
            payment.setTransactionId(UUID.randomUUID().toString());
            paymentRepository.save(payment);
            logger.info("Payment processed.");
        }else{
            payment.setOrderStatus("failed");
            logger.info("Payment failed.");
        }
        return payment;
    }

    public boolean paymentProcessing(){
        return new Random().nextBoolean();
    }

    public Payment findPaymentHistoryByOrderId(String orderUniqueId) throws JsonProcessingException {
        Payment p = paymentRepository.findByOrderUniqueId(orderUniqueId);
        logger.info("Payment search result " + new ObjectMapper().writeValueAsString(p));
        return p;
    }
}
