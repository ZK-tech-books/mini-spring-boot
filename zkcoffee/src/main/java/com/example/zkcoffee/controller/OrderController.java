package com.example.zkcoffee.controller;

import com.example.zkcoffee.service.OrderService;
import minispring.beans.factory.annotation.Autowired;
import minispring.web.bind.annotation.GetMapping;
import minispring.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/stock")
    public int stock() {
        return orderService.findStock("coffee");
    }
}
