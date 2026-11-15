# mini-spring-boot

Spring Boot の中核の仕組みをまねて作った小さなフレームワーク minispring のリポジトリです。

## 機能

- [x] IoC コンテナ
- [x] Spring Web MVC
- [x] 組み込み Tomcat の起動
- [x] `@Transactional`

## 構成

| プロジェクト | 中身 |
| --- | --- |
| minispring | Spring Boot をまねたフレームワーク |
| zkcoffee | minispring で作ったアプリケーション |

## 使用技術

| カテゴリ | 技術 |
|---|---|
| 言語 | Java 25 |
| ビルドツール | Gradle |
| Web サーバ | Apache Tomcat |
| JSON 変換 | Jackson |
| プロキシ生成 | Byte Buddy |

## 起動方法

```sh
./gradlew :zkcoffee:run
```

```sh
curl localhost:8080/stock
# {"productName":"coffee","quantity":10}

curl -X POST localhost:8080/orders
# Ordered

curl localhost:8080/stock
# {"productName":"coffee","quantity":9}
```
