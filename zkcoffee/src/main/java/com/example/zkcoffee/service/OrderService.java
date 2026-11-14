package com.example.zkcoffee.service;

import com.example.zkcoffee.repository.OrderRepository;
import com.example.zkcoffee.repository.StockRepository;
import minispring.beans.factory.annotation.Autowired;
import minispring.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void order(String itemId) {
        this.stockRepository.decrease(itemId);
        this.orderRepository.save(itemId);
    }

    public int findStock(String itemId) {
        return this.stockRepository.findStock(itemId);
    }
}
