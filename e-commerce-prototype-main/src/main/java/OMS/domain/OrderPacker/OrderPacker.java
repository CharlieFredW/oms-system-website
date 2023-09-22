package OMS.domain.OrderPacker;

import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderStatus;
import OMS.domain.Mail;
import OMS.domain.OrderCRUD.OrderCRUD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderPacker implements IPackOrders {

    OrderCRUD orderCRUD = new OrderCRUD();
    Connection connection = orderCRUD.getConnection();

    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET status = ? WHERE id = ?")) {
            stmt.setObject(1, status, Types.OTHER);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            Mail mail = new Mail(orderCRUD.getEmailByOrderID(orderId), "Order status update ORD#" + orderId, "Your order status has been updated to: " + status);
            return mail.sendMail();
        } catch (Exception e) {
            return false;
        }
    }

    public OrderStatus getOrderStatus(int orderId) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT status FROM orders where id = ?")) {
            stmt.setInt(1, orderId);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();
            return OrderStatus.valueOf(rs.getString("status"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getShippingInfo(int id) {
        ArrayList<String> shippingInfo = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT name, address, city, zip, country FROM orders WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            shippingInfo.add("Name: " + rs.getString(1));
            shippingInfo.add("Address: " + rs.getString(2));
            shippingInfo.add("City: " + rs.getString(3));
            shippingInfo.add("ZipCode: " + rs.getString(4));
            shippingInfo.add("Country: " + rs.getString(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shippingInfo;
    }

    public boolean sendNotificationToCourier(int id) {
        Mail mail = new Mail("chillycharlie1@gmail.com", "Order Status Update", "Order: #" + id + "\n" + "Has Been Processed And Is Ready To Be Shipped");
        mail.sendMail();
        return true;
    }

    // Method to get orders from DB. Between two dates. Only from selected order statuses, unless order id(s) is specified.
    public ArrayList<Order> getOrders(LocalDate startDate, LocalDate endDate, Boolean processing,
                                      Boolean processed, Boolean packaged, Boolean shipped,
                                      Boolean delivered, Boolean failed, int... orderIds) {
        ArrayList<Order> orders = new ArrayList<>();

        // Create an ArrayList of selected order statuses to use in the sql query
        ArrayList<OrderStatus> OrderStatuses = new ArrayList<>();
        if (processing) {
            OrderStatuses.add(OrderStatus.PROCESSING);
        }
        if (processed) {
            OrderStatuses.add(OrderStatus.PROCESSED);
        }
        if (packaged) {
            OrderStatuses.add(OrderStatus.PACKAGED);
        }
        if (shipped) {
            OrderStatuses.add(OrderStatus.SHIPPED);
        }
        if (delivered) {
            OrderStatuses.add(OrderStatus.DELIVERED);
        }
        if (failed) {
            OrderStatuses.add(OrderStatus.FAILED);
        }


        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);
            OrderCRUD orderCRUD = new OrderCRUD();

            // Convert int[] oderIds to Array
            Integer[] orderIdsIntegerArray = Arrays.stream(orderIds).boxed().toArray(Integer[]::new);
            Array orderIdsArray = connection.createArrayOf("integer", orderIdsIntegerArray);


            // convert status ArrayList to Array
            OrderStatus[] orderStatusArray = OrderStatuses.toArray(new OrderStatus[OrderStatuses.size()]);
            Array orderStatusArray2 = connection.createArrayOf("orderstatus", orderStatusArray);

            // Prepared statement
            PreparedStatement stmt;
            if (orderIds.length == 0) {
                stmt = connection.prepareStatement("SELECT * FROM orders WHERE status = ANY (?) AND created BETWEEN ? AND ?");
                stmt.setArray(1, orderStatusArray2);
            } else {
                stmt = connection.prepareStatement("SELECT * FROM orders WHERE id = ANY (?) AND created BETWEEN ? AND ?");
                stmt.setArray(1, orderIdsArray);
            }
            stmt.setTimestamp(2, Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(3, Timestamp.valueOf(endDateTime));


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                int zip = rs.getInt("zip");
                String city = rs.getString("city");
                String country = rs.getString("country");
                Timestamp orderDate = rs.getTimestamp("created");
                double totalPrice = rs.getDouble("total_price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
                double vat = rs.getDouble("vat");

                orders.add(new Order(id, name, email, phone, address, zip, city, country, orderDate, totalPrice, status, vat));
                // System.out.println(id);
            }

            stmt.close();

            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
