package com.hao.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hao.paymentservice.entity.Payment;
import com.hao.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;
    @Autowired
    public void setPaymentService(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping("/doPayments")
    public Payment doPayment(@RequestBody Payment payment){
        return paymentService.executePayment(payment);
    }
    @GetMapping("/{orderUniqueId}")
    public Payment findPaymentHistoryByUniqueId(@PathVariable String orderUniqueId) throws JsonProcessingException {
        return paymentService.findPaymentHistoryByOrderId(orderUniqueId);
    }
}
