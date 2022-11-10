package com.hao.cloudgateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/orderFallBack")
    public Mono<String> orderServiceFallBack(){
        return Mono.just("Order service not responding. Try again later");
    }
    @RequestMapping("/paymentFallBack")
    public Mono<String> paymentServiceFallBack(){
        return Mono.just("Payment service not responding. Try again later");
    }
    @RequestMapping("/inventoryFallBack")
    public Mono<String> inventoryServiceFallBack(){
        return Mono.just("Inventory service not responding. Try again later");
    }
}
