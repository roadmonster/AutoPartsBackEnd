package com.hao.orderservice3.service;

import com.hao.orderservice3.Common.*;
import com.hao.orderservice3.entity.Order;
import com.hao.orderservice3.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

@Service
@RefreshScope
public class OrderService {
    private OrderRepository orderRepository;
    private RestTemplate template;

    @Autowired
    public void setOrderRepository(OrderRepository repository){
        this.orderRepository = repository;
    }

    @Autowired
    @Lazy
    public void setTemplate(RestTemplate template){
        this.template = template;
    }
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String PAY_URL;

    @Value("${microservice.inventory-update-service.endpoints.endpoint.uri}")
    private String INVENTORY_UPDATE_URL;

    @Value("${microservice.inventory-check-service.endpoints.endpoint.uri}")
    private String INVENTORY_CHECK_URL;

    /**
     * Logic for placing order
     * @param request order request
     * @return order response object
     */
    public OrderResponse placeOrder(OrderRequest request){
        OrderResponse response = new OrderResponse();

        InventoryResponse inventoryStatus = checkInventory(request);
        int unitAvailable = inventoryStatus.getAvailableUnits();
        System.out.println("feed back for part amount: " + unitAvailable);
        if (unitAvailable < request.getOrder().getQty()){
            response.setMessage("Insufficient stock, available amount " + unitAvailable);
            return response;
        }
        Payment result = processPayment(request);
        if (result == null){
            response.setMessage("Payment failed. Please different payment method");
            return response;
        }
        inventoryStatus = executeInventoryOrder(request);
        System.out.println(inventoryStatus.toString());
        if (!inventoryStatus.isOrderFulfilled()){
            response.setMessage("Back order now. We will issue the refund within 7 days.");
            return response;
        }
        Order myOrder = orderRepository.save(request.getOrder());
        return new OrderResponse(myOrder.getId(), myOrder.getPrice(),
                "Order successfully executed", myOrder.getOrderUniqueId());
    }
    private void getOrderUniqueId(OrderRequest request){
        request.getOrder().setOrderUniqueId(UUID.randomUUID().toString());
    }

    private Payment processPayment(OrderRequest request){
        getOrderUniqueId(request);
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setAmount(order.getPrice());
        logger.info("Sending request to payment service");
        return template.postForObject(PAY_URL, payment, Payment.class);
    }

    private InventoryResponse checkInventory(OrderRequest request){
        logger.info("Checking inventory for item.");
        Order order = request.getOrder();
        return template.getForObject(INVENTORY_CHECK_URL + "/" + order.getStockId(),
                InventoryResponse.class);
    }

    private InventoryResponse executeInventoryOrder(OrderRequest request){
        logger.info("Execute inventory request.");
        Order order = request.getOrder();
        InventoryRequest req = new InventoryRequest(order.getStockId(), order.getQty(), order.getProduct());
        return template.postForObject(INVENTORY_UPDATE_URL, req, InventoryResponse.class);

    }
}
