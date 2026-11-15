package com.example.zkcoffee.response;

/**
 * 在庫の確認で返すレスポンス。
 *
 * @param productName 商品の名前
 * @param quantity 在庫の数
 */
public record GetStockResponse(String productName, int quantity) {
}
