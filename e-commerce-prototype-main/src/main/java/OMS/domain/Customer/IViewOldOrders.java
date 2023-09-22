package OMS.domain.Customer;

import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderProducts;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IViewOldOrders {

    LinkedHashMap<Integer, LinkedHashMap<Order, ArrayList<OrderProducts>>> getOrdersAndProductsByEmail(String customerEmail);

    LinkedHashMap<Integer, Order> getOrdersByEmail(String customerEmail);
}
