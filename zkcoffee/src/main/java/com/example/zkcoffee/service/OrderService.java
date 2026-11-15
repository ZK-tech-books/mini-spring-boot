package com.example.zkcoffee.service;

import com.example.zkcoffee.repository.OrderRepository;
import com.example.zkcoffee.repository.StockRepository;
import minispring.beans.factory.annotation.Autowired;
import minispring.stereotype.Service;
import minispring.transaction.annotation.Transactional;

/**
 * 注文と在庫の確認を受け持つサービス。
 */
@Service
public class OrderService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 商品を1つ注文する。
     * <p>
     * 在庫を1つ減らし、注文を記録する。
     *
     * @param productName 注文する商品の名前
     */
    @Transactional
    public void order(String productName) {
        this.stockRepository.decrease(productName);
        this.orderRepository.save(productName);
    }

    /**
     * 商品の在庫の数を返す。
     *
     * @param productName 商品の名前
     * @return 在庫の数
     * @throws java.util.NoSuchElementException 商品がないとき
     */
    public int getStock(String productName) {
        return this.stockRepository.findStock(productName).orElseThrow();
    }
}
