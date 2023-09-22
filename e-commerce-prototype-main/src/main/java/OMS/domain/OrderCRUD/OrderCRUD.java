package OMS.domain.OrderCRUD;

import OMS.data.Database.OrderDatabase;
import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderProducts;
import OMS.domain.Mail;
import OMS.domain.CreateOrders.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderCRUD implements IOrderCRUD {

    private final IDatabase database = OrderDatabase.getInstance();

    private final Connection connection = database.getConnection();

    public Connection getConnection() {
        return connection;
    }

    public int getNewestOrderId() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM orders ORDER BY created DESC LIMIT 1")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new RuntimeException("No orders found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //add an order to the database from the order created by the string in orderCRUD
    public boolean createOrder(CreateOrder order) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (name, email, phone, address, city, zip, country, total_price, status, vat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, order.getOrder().getFullName());
            stmt.setString(2, order.getOrder().getMail());
            stmt.setInt(3, order.getOrder().getPhoneNumber());
            stmt.setString(4, order.getOrder().getAddress());
            stmt.setString(5, order.getOrder().getCity());
            stmt.setInt(6, order.getOrder().getZipCode());
            stmt.setString(7, order.getOrder().getCountry());
            stmt.setDouble(8, order.getOrder().getTotalPrice());
            stmt.setObject(9, order.getOrder().getStatus(), Types.OTHER);
            stmt.setDouble(10, order.getOrder().getVat());
            stmt.execute();

            Mail mail_confirmation = new Mail(order.getOrder().getMail(), "Order confirmation", "Your order has been received and is being processed.\n Order ID: ORD#" + getNewestOrderId() + "\n Total price: " + order.getOrder().getTotalPrice() + " DKK");
            mail_confirmation.sendMail();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //method to delete a specific order and its order info
    public boolean deleteOrder(int orderId) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE from order_info WHERE order_id = ?");
             PreparedStatement stmt1 = connection.prepareStatement("DELETE from orders WHERE id = ?")) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();

            stmt1.setInt(1, orderId);
            stmt1.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    //method to update an order
    public boolean updateOrder(int Id, int orderId, int productId, int Quantity, double price) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE from order_info WHERE id = ?");
             PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO order_info (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)")) {

            stmt.setInt(1, Id);
            stmt.executeUpdate();
            stmt2.setInt(1, orderId);
            stmt2.setInt(2, productId);
            stmt2.setInt(3, Quantity);
            stmt2.setDouble(4, price);
            stmt2.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    //method to add the products to the database
    public boolean createProduct(CreateOrder orderProducts) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO order_info (order_id, product_id, quantity, price) " +
                        "VALUES (?, ?, ?, ?)")
        ) {
            int orderId = getNewestOrderId();
            for (OrderProducts products : orderProducts.getProductsList()) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, products.getProductId());
                stmt.setInt(3, products.getAmount());
                stmt.setDouble(4, products.getPrice());
                stmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //method to get the order status from the database
    public OrderStatus getOrderStatus(int orderId) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT status FROM orders WHERE id = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return OrderStatus.valueOf(rs.getString("status"));
            } else {
                throw new RuntimeException("No orders found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmailByOrderID(int orderId) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT email FROM orders WHERE id = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            } else {
                throw new RuntimeException("No orders found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Order> getCurrentOrders() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders WHERE status != 'DELIVERED'")) {
            ResultSet rs = stmt.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                int zip = rs.getInt("zip");
                String city = rs.getString("city");
                String country = rs.getString("country");
                double totalPrice = rs.getDouble("total_price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
                double vat = rs.getDouble("vat");

                orders.add(new Order(name, email, phone, address, zip, city, country, totalPrice, status, vat));
            }

            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Order> getOrdersCRUD(int... orderIDs) {
        ArrayList<Order> orders = new ArrayList<>();

        OrderCRUD orderCRUD = new OrderCRUD();

        try (PreparedStatement stmt = orderCRUD.getConnection().prepareStatement("SELECT * FROM orders WHERE id = ANY (?)")) {

            Integer[] orderIdsIntegerArray = Arrays.stream(orderIDs).boxed().toArray(Integer[]::new);
            Array orderIdsArray = orderCRUD.getConnection().createArrayOf("integer", orderIdsIntegerArray);
            stmt.setArray(1, orderIdsArray);
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
                System.out.println(id);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return orders;
    }

}
