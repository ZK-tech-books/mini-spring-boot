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

    public void order(String productId) {
        this.stockRepository.decrease(productId);
        this.orderRepository.save(productId);
    }

    public int findStock(String productId) {
        return this.stockRepository.findStock(productId);
    }
}
