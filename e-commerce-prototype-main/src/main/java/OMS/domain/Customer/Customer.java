package OMS.domain.Customer;

import OMS.UI.OtherGroupsAPIS.IViewOrder;
import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderProducts;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.CreateOrders.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Customer implements IViewOrder, IViewOldOrders {

    private final OrderCRUD orderCRUD = new OrderCRUD();
    private final Connection connection = orderCRUD.getConnection();

    public LinkedHashMap<Integer, LinkedHashMap<Order, ArrayList<OrderProducts>>> getOrdersAndProductsByEmail(String customerEmail) {
        LinkedHashMap<Integer, Order> orders = getOrdersByEmail(customerEmail);
        LinkedHashMap<Integer, LinkedHashMap<Order, ArrayList<OrderProducts>>> completeOrder = new LinkedHashMap<>();
        for (Map.Entry<Integer, Order> order :
                orders.entrySet()) {
            completeOrder.put(order.getKey(), new LinkedHashMap<>() {{
                put(order.getValue(), getProductsInOrder(order.getKey()));
            }});
        }
        return completeOrder;
    }

    //method to get previous orders from the database with specified email
    // TODO --> get Orders and products in the order (this only gets the orders)
    public LinkedHashMap<Integer, Order> getOrdersByEmail(String customerEmail) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders WHERE email = ? ORDER BY created DESC")) {
            stmt.setString(1, customerEmail);
            ResultSet rs = stmt.executeQuery();
            LinkedHashMap<Integer, Order> orders = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int zip = rs.getInt("zip");
                String country = rs.getString("country");
                double price = rs.getDouble("total_price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
                double vat = rs.getDouble("vat");
                Order order = new Order(name, email, phone, address, zip, city, country, price, status, vat);
                orders.put(id, order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<OrderProducts> getProductsInOrder(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT product_id, quantity, price FROM order_info WHERE order_id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            ArrayList<OrderProducts> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new OrderProducts(
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getInt("price"))
                );
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
