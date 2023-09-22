package OMS.domain.OrderPacker;

import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderStatus;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IPackOrders {
    ArrayList<Order> getOrders(LocalDate startDate, LocalDate endDate, Boolean processing, Boolean processed,
                               Boolean packaged, Boolean shipped, Boolean delivered, Boolean failed, int... orderIds);

    boolean updateOrderStatus(int orderId, OrderStatus status);

    OrderStatus getOrderStatus(int orderId);

    ArrayList<String> getShippingInfo(int id);

    boolean sendNotificationToCourier(int id);

}
