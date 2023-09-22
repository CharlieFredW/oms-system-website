package OMS.UI.OtherGroupsAPIS;

import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderProducts;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IViewOrder {
    //method for shop to get customers orders viewed
    LinkedHashMap<Integer, LinkedHashMap<Order, ArrayList<OrderProducts>>> getOrdersAndProductsByEmail(String customerEmail);

    LinkedHashMap<Integer, Order> getOrdersByEmail(String customerEmail);

}
