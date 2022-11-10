package com.hao.demo.service;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Map;

@Service
public class CacheService {
    private final Map<Integer, Pair<Timestamp, Integer>> stockCache;
    private final Map<Integer, String> itemList;

    @Autowired
    public CacheService(Map<Integer, Pair<Timestamp, Integer>> stockCache,
                        Map<Integer, String> itemList) {
        this.stockCache = stockCache;
        this.itemList = itemList;
    }

    public boolean carryItem(int stockId){
        return itemList.containsKey(stockId);
    }

    public void unCarry(int stockId){
        itemList.remove(stockId);
        stockCache.remove(stockId);
    }

    public void newComingItem(int stockId, String productName){
        this.itemList.put(stockId, productName);
    }

    public int getStockUnits(int stockId){
        return stockCache.get(stockId).getValue();
    }

    public boolean checkExpiration(int stockId) {
        Timestamp ts = this.stockCache.get(stockId).getKey();
        long cacheTime = ts.getTime();
        final int VALID_MILLISECONDS = 20000;
        return cacheTime + VALID_MILLISECONDS > System.currentTimeMillis();
    }

    public void setOrFlushStock(int stockId, int stockUnits) {
        this.stockCache.put(stockId,
                new Pair<>(new Timestamp(System.currentTimeMillis()), stockUnits));
    }

    public String getStockName(int stockId){
        return this.itemList.get(stockId);
    }
}
