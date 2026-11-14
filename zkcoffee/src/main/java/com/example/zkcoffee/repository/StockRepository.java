package com.example.zkcoffee.repository;

import java.util.HashMap;
import java.util.Map;
import minispring.stereotype.Repository;

@Repository
public class StockRepository {
    private final Map<String, Integer> stocks = new HashMap<>(Map.of("coffee", 10));

    public int findStock(String productId) {
        return this.stocks.get(productId);
    }

    public void decrease(String productId) {
        this.stocks.merge(productId, -1, Integer::sum);
    }
}
