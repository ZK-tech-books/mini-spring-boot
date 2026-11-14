package com.example.zkcoffee.repository;

import java.util.HashMap;
import java.util.Map;
import minispring.stereotype.Repository;

@Repository
public class StockRepository {
    private final Map<String, Integer> stocks = new HashMap<>(Map.of("coffee", 10));

    public int findStock(String itemId) {
        return this.stocks.get(itemId);
    }

    public void decrease(String itemId) {
        this.stocks.merge(itemId, -1, Integer::sum);
    }
}
