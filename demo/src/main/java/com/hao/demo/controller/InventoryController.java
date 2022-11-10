package com.hao.demo.controller;

import com.hao.demo.entity.Inventory;
import com.hao.demo.entity.InventoryRequest;
import com.hao.demo.entity.InventoryResponse;
import com.hao.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    public void setInventoryService(InventoryService service){
        this.inventoryService = service;
    }

    @PostMapping("/fill")
    public Inventory fill(@RequestBody Inventory i){
        return inventoryService.saveInventory(i);
    }

    @PostMapping("/update")
    public InventoryResponse update(@RequestBody InventoryRequest request){
        return inventoryService.updateInventory(request);
    }

    @GetMapping("/{stockId}")
    public InventoryResponse checkStock(@PathVariable int stockId){
        return inventoryService.checkInventory(stockId);
    }

    @PostMapping("/deletion/{stockId}")
    public InventoryResponse deleteStock(@PathVariable int stockId){
        return inventoryService.deleteInventory(stockId);
    }
}