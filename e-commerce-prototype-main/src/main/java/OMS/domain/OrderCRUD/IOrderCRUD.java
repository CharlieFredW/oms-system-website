package OMS.domain.OrderCRUD;

import OMS.domain.CreateOrders.Order;

import java.util.ArrayList;

public interface IOrderCRUD {

    //method to get an arraylist of orders based on order id = read an order
    ArrayList<Order> getOrdersCRUD(int... orderId);


    //method to update an order
    boolean updateOrder(int Id, int orderID, int productID, int Quantity, double price);


    //method to delete an order
    boolean deleteOrder(int orderId);
}
