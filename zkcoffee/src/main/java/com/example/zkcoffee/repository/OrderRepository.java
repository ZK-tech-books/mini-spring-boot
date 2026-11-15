package com.example.zkcoffee.repository;

import java.util.ArrayList;
import java.util.List;
import minispring.stereotype.Repository;

@Repository
public class OrderRepository {
    private final List<String> orders = new ArrayList<>();

    public void save(String productName) {
        this.orders.add(productName);
    }

    public int count() {
        return this.orders.size();
    }
}
