package com.hao.orderservice3.repository;


import com.hao.orderservice3.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
