package com.example.zkcoffee.controller;

import com.example.zkcoffee.response.GetStockResponse;
import com.example.zkcoffee.service.OrderService;
import minispring.beans.factory.annotation.Autowired;
import minispring.web.bind.annotation.GetMapping;
import minispring.web.bind.annotation.PostMapping;
import minispring.web.bind.annotation.RestController;

/**
 * 在庫の確認と注文を受け付けるコントローラ。
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * コーヒーの在庫の数を返す。
     *
     * @return 商品の名前と在庫の数
     */
    @GetMapping("/stock")
    public GetStockResponse getStock() {
        return new GetStockResponse("coffee", this.orderService.getStock("coffee"));
    }

    /**
     * コーヒーを1つ注文する。
     *
     * @return 注文を受け付けたことを表す文字列
     */
    @PostMapping("/orders")
    public String order() {
        this.orderService.order("coffee");
        return "Ordered";
    }
}
