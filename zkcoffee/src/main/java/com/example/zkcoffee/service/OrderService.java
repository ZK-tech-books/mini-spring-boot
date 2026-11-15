package com.example.zkcoffee.service;

import com.example.zkcoffee.repository.OrderRepository;
import com.example.zkcoffee.repository.StockRepository;
import minispring.beans.factory.annotation.Autowired;
import minispring.stereotype.Service;
import minispring.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void order(String productName) {
        this.stockRepository.decrease(productName);
        this.orderRepository.save(productName);
    }

    public int getStock(String productName) {
        return this.stockRepository.findStock(productName).orElseThrow();
    }
}
