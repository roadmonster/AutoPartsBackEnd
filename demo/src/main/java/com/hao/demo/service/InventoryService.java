package com.hao.demo.service;

import com.hao.demo.entity.Inventory;
import com.hao.demo.entity.InventoryRequest;
import com.hao.demo.entity.InventoryResponse;
import com.hao.demo.repository.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class InventoryService {
    private InventoryRepository repository;
    private CacheService cache;
    private Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public void setRepository(InventoryRepository repository){
        this.repository = repository;
    }
    @Autowired
    public void setCacheService(CacheService cache){
        this.cache = cache;
    }


    /**
     * Save new inventory into database
     * @param inventory inventory item holding productName, availableUnits
     * @return inventory object
     */
    public Inventory saveInventory(Inventory inventory){
        Inventory saved = this.repository.save(inventory);
        int stockId = saved.getId();

        if (!this.cache.carryItem(stockId)){
            logger.debug("Adding item");;
            this.cache.newComingItem(stockId, inventory.getProductName());
        }
        this.cache.setOrFlushStock(stockId, inventory.getUnitsAvailable());
        return saved;
    }

    /**
     * Update the inventory of given item
     * @param request request holding stockId, order quantity, productName
     * @return InventoryResponse object
     */
    public InventoryResponse updateInventory(InventoryRequest request){
        InventoryResponse currInventory = checkInventory(request.getStockId());
        int orderQty = request.getOrderQty();
        logger.info("order qty " + orderQty);
        logger.info("avail qty " + currInventory.getAvailableUnits());
        if (currInventory.getAvailableUnits() >= orderQty){
            int newStockUnits = currInventory.getAvailableUnits() - orderQty;
            saveInventory(new Inventory(request.getStockId(), request.getProductName(), newStockUnits));
            currInventory.setAvailableUnits(newStockUnits);
            currInventory.setOrderFulfilled(true);
            logger.info("updated inventory");
        }
        else{
            currInventory.setOrderFulfilled(false);
            logger.info("failed updating");
        }
        return currInventory;
    }

    /**
     * Check the inventory status, avoid repeat access of DB with cache
     * @param stockId id to check for availability
     * @return InventoryResponse object
     */
    public InventoryResponse checkInventory(int stockId){
        InventoryResponse response =
                new InventoryResponse(stockId, -1, "Name_Unavailable", false);
        if (!cache.carryItem(stockId)){
            logger.debug("Not carrying this item");
            return response;
        }
        response.setProductName(cache.getStockName(stockId));
        if (cache.checkExpiration(stockId)){
            logger.debug("cache is valid, using cache");
            int availableUnits = cache.getStockUnits(stockId);
            response.setAvailableUnits(availableUnits);
            response.setOrderFulfilled(true);
        }else{
            logger.debug("cache expired, hitting db");
            Optional<Inventory> resultOptional = this.repository.findById(stockId);
            resultOptional.ifPresent((Inventory result)->{
                int units = result.getUnitsAvailable();
                cache.setOrFlushStock(stockId, units);
                response.setOrderFulfilled(true);
                response.setAvailableUnits(units);
            });
        }
        return response;
    }

    /**
     * Delete inventory by id
     * @param stockId stockId to be deleted
     * @return InventoryResponse object
     */
    public InventoryResponse deleteInventory(int stockId){
        if (this.cache.carryItem(stockId)){
            this.repository.deleteById(stockId);
            this.cache.unCarry(stockId);
        }
        return new InventoryResponse(stockId, -1, "unavailable", true);
    }
}


