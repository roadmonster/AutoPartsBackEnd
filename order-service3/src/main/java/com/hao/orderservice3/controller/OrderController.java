package com.hao.orderservice3.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hao.orderservice3.Common.OrderRequest;
import com.hao.orderservice3.Common.OrderResponse;
import com.hao.orderservice3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orderParts")
    public OrderResponse orderParts(@RequestBody OrderRequest request) throws JsonProcessingException {
        return orderService.placeOrder(request);
    }
}
