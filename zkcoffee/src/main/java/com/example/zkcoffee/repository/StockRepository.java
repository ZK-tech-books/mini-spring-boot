package com.example.zkcoffee.repository;

import java.util.HashMap;
import java.util.Map;
import minispring.stereotype.Repository;

@Repository
public class StockRepository {
    private final Map<String, Integer> stocks = new HashMap<>(Map.of("coffee", 10));

    public int findStock(String productName) {
        return this.stocks.get(productName);
    }

    public void decrease(String productName) {
        this.stocks.merge(productName, -1, Integer::sum);
    }
}
