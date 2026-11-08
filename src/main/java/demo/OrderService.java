package demo;

import minispring.Autowired;
import minispring.Component;

@Component
public class OrderService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void order(String itemId) {
        stockRepository.decrease(itemId);
        orderRepository.save(itemId);
    }
}
