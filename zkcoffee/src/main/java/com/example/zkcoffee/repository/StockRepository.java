package com.example.zkcoffee.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import minispring.stereotype.Repository;

/**
 * 商品ごとの在庫の数を持つリポジトリ。
 */
@Repository
public class StockRepository {
    private final Map<String, Integer> stocks = new HashMap<>(Map.of("coffee", 10));

    /**
     * 商品の在庫の数を探す。
     *
     * @param productName 商品の名前
     * @return 在庫の数。商品がなければ空の {@link Optional}
     */
    public Optional<Integer> findStock(String productName) {
        return Optional.ofNullable(this.stocks.get(productName));
    }

    /**
     * 商品の在庫の数を1つ減らす。
     *
     * @param productName 商品の名前
     */
    public void decrease(String productName) {
        this.stocks.merge(productName, -1, Integer::sum);
    }
}
