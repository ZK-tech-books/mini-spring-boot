package demo;

import java.util.HashMap;
import java.util.Map;
import minispring.Component;

@Component
public class StockRepository {
    private final Map<String, Integer> stocks = new HashMap<>(Map.of("coffee", 10));

    public int findStock(String itemId) {
        return stocks.get(itemId);
    }

    public void decrease(String itemId) {
        stocks.merge(itemId, -1, Integer::sum);
    }
}
