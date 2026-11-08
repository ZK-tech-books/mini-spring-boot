package demo;

import minispring.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("demo");
        StockRepository stockRepository = context.getBean(StockRepository.class);
        OrderRepository orderRepository = context.getBean(OrderRepository.class);

        System.out.println("Stock before: " + stockRepository.findStock("coffee"));
        context.getBean(OrderService.class).order("coffee");
        System.out.println("Stock after: " + stockRepository.findStock("coffee"));
        System.out.println("Orders: " + orderRepository.count());
    }
}
