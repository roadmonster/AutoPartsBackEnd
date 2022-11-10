package com.hao.inventoryservice.controller;

import com.hao.inventoryservice.entity.Inventory;
import com.hao.inventoryservice.entity.InventoryRequest;
import com.hao.inventoryservice.entity.InventoryResponse;
import com.hao.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    public void setInventoryService(InventoryService service){
        this.inventoryService = service;
    }
    @PostMapping("/insert")
    public Inventory insert(@RequestBody Inventory i){
        return inventoryService.saveInventory(i);
    }

    @PostMapping("/update")
    public InventoryResponse update(@RequestBody InventoryRequest request){
        return inventoryService.updateInventory(request);
    }
}
