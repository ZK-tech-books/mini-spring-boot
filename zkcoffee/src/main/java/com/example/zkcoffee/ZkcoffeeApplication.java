package com.example.zkcoffee;

import minispring.boot.SpringApplication;

/**
 * ZK珈琲のアプリを起動するクラス。
 */
public class ZkcoffeeApplication {
    /**
     * minispring でアプリを起動する。
     *
     * @param args コマンドライン引数
     * @throws Exception 起動に失敗したとき
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ZkcoffeeApplication.class, args);
    }
}
