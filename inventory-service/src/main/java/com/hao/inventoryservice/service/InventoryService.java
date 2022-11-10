package com.hao.inventoryservice.service;

import com.hao.inventoryservice.entity.Inventory;
import com.hao.inventoryservice.entity.InventoryRequest;
import com.hao.inventoryservice.entity.InventoryResponse;
import com.hao.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InventoryService {
    private InventoryRepository repository;

    @Autowired
    public void setRepository(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory saveInventory(Inventory inventory){
        return this.repository.save(inventory);
    }

    public InventoryResponse updateInventory(InventoryRequest request){
        AtomicBoolean fulfilled = new AtomicBoolean(false);
        Optional<Inventory> resultOptional = this.repository.findById(request.getId());
        resultOptional.ifPresent((Inventory result) ->{
            int amt = result.getUnitsAvailable();
            int qty = request.getOrderQty();
            if (amt > qty){
                fulfilled.set(true);
                this.saveInventory(new Inventory(result.getId(), result.getProductName(), amt - qty));
            }
        });
        return new InventoryResponse(fulfilled.get());
    }
}
