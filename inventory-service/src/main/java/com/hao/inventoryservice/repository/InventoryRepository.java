package com.hao.inventoryservice.repository;

import com.hao.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
