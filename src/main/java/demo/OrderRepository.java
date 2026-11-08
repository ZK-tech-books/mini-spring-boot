package demo;

import java.util.ArrayList;
import java.util.List;
import minispring.Component;

@Component
public class OrderRepository {
    private final List<String> orders = new ArrayList<>();

    public void save(String itemId) {
        orders.add(itemId);
    }

    public int count() {
        return orders.size();
    }
}
