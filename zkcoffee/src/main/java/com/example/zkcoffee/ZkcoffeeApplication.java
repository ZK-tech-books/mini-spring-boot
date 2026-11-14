package com.example.zkcoffee;

import com.example.zkcoffee.repository.OrderRepository;
import com.example.zkcoffee.repository.StockRepository;
import com.example.zkcoffee.service.OrderService;
import minispring.context.ApplicationContext;

public class ZkcoffeeApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("com.example.zkcoffee");
        StockRepository stockRepository = context.getBean(StockRepository.class);
        OrderRepository orderRepository = context.getBean(OrderRepository.class);

        System.out.println("Stock before: " + stockRepository.findStock("coffee"));
        context.getBean(OrderService.class).order("coffee");
        System.out.println("Stock after: " + stockRepository.findStock("coffee"));
        System.out.println("Orders: " + orderRepository.count());
    }
}
