package com.example.zkcoffee.repository;

import java.util.ArrayList;
import java.util.List;
import minispring.stereotype.Repository;

/**
 * 受け付けた注文を記録するリポジトリ。
 */
@Repository
public class OrderRepository {
    private final List<String> orders = new ArrayList<>();

    /**
     * 注文を記録する。
     *
     * @param productName 注文した商品の名前
     */
    public void save(String productName) {
        this.orders.add(productName);
    }

    /**
     * 記録した注文の数を返す。
     *
     * @return 注文の数
     */
    public int count() {
        return this.orders.size();
    }
}
