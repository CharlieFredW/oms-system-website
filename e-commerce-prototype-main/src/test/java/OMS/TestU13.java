package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.CreateOrders.Order;
import OMS.domain.CreateOrders.OrderProducts;
import OMS.domain.Customer.Customer;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.OrderPacker.OrderPacker;
import OMS.domain.CreateOrders.OrderStatus;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TestU13 {

    //daniels test

    static String email;
    static JSONObject testOrder;
    static JSONObject pastOrder;
    static int orderId;
    static OrderPacker orderPacker;
    static CreateOrder createOrder = new CreateOrder();

    @BeforeAll
    static void setUp() {
        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U13testOrder.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U13testOrder.json");
        }


        try {
            File pastOrder_file = new File("src/test/java/OMS/jsonTestOrders/U13pastOrder.json");
            InputStream past_is = new FileInputStream(pastOrder_file);
            pastOrder = new JSONObject(new JSONTokener(past_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U13pastOrder.json");
        }

        if (Objects.equals(testOrder.getJSONObject("customer").getString("mail"), pastOrder.getJSONObject("customer").getString("mail"))) {
            email = testOrder.getJSONObject("customer").getString("mail");
        } else {
            throw new RuntimeException("Emails do not match");
        }

        new CreateOrder().CreateOrder(pastOrder);
        createOrder.CreateOrder(testOrder);
        orderPacker = new OrderPacker();
        orderId = new OrderCRUD().getNewestOrderId();
    }

    // Is the order status marked as PROCESSED?
    @Test
    void markOrderAsProcessed() {
        orderPacker.updateOrderStatus(orderId, OrderStatus.PROCESSED);
        assertSame(orderPacker.getOrderStatus(orderId), OrderStatus.PROCESSED);
    }

    // Is the order status marked as SHIPPED?
    @Test
    void markOrderAsShipped() {
        orderPacker.updateOrderStatus(orderId, OrderStatus.SHIPPED);
        assertSame(orderPacker.getOrderStatus(orderId), OrderStatus.SHIPPED);
    }

    // Get current & past orders by email and the products in the orders
    @Test
    void currentPastOrdersByEmail() {
        LinkedHashMap<Integer, LinkedHashMap<Order, ArrayList<OrderProducts>>> completeOrders = new Customer().getOrdersAndProductsByEmail(email);
        assertTrue(completeOrders.size() > 0);

        // Current orders
        assertTrue(completeOrders.get(orderId).size() > 0);
        completeOrders.get(orderId).forEach((order, orderProducts) -> {
            assertEquals(order.getFullName(), "bruhbruh bruhman");
            assertEquals(order.getMail(), email);
            assertEquals(order.getPhoneNumber(), 4637281);
            assertEquals(order.getAddress(), "Millevej 21");
            assertEquals(order.getZipCode(), 3829);
            assertEquals(order.getCity(), "Milleby");
            assertEquals(order.getTotalPrice(), 34648);
            assertEquals(order.getVat(), 20);
            assertEquals(order.getStatus(), OrderStatus.PROCESSED);
            orderProducts.forEach(orderProduct -> {
                switch (orderProduct.getProductId()) {
                    case 1 -> {
                        assertEquals(orderProduct.getAmount(), 1);
                        assertEquals(orderProduct.getPrice(), 7000);
                    }
                    case 2 -> {
                        assertEquals(orderProduct.getAmount(), 3);
                        assertEquals(orderProduct.getPrice(), 150);
                    }
                    case 5 -> {
                        assertEquals(orderProduct.getAmount(), 2);
                        assertEquals(orderProduct.getPrice(), 12999);
                    }
                    case 7 -> {
                        assertEquals(orderProduct.getAmount(), 1);
                        assertEquals(orderProduct.getPrice(), 1200);
                    }
                    default -> fail("Product not found");
                }
            });

        });

        // Past orders
        assertTrue(completeOrders.get(orderId - 1).size() > 0);
        completeOrders.get(orderId - 1).forEach((order, orderProducts) -> {
            assertEquals(order.getFullName(), "bruhbruh bruhman");
            assertEquals(order.getMail(), email);
            assertEquals(order.getPhoneNumber(), 4637281);
            assertEquals(order.getAddress(), "Millevej 21");
            assertEquals(order.getZipCode(), 3829);
            assertEquals(order.getCity(), "Milleby");
            assertEquals(order.getTotalPrice(), 55198);
            assertEquals(order.getVat(), 20);
            assertEquals(order.getStatus(), OrderStatus.PROCESSED);
            orderProducts.forEach(orderProduct -> {
                switch (orderProduct.getProductId()) {
                    case 2 -> {
                        assertEquals(orderProduct.getAmount(), 1);
                        assertEquals(orderProduct.getPrice(), 7000);
                    }
                    case 3 -> {
                        assertEquals(orderProduct.getAmount(), 3);
                        assertEquals(orderProduct.getPrice(), 7000);
                    }
                    case 5 -> {
                        assertEquals(orderProduct.getAmount(), 2);
                        assertEquals(orderProduct.getPrice(), 12999);
                    }
                    case 7 -> {
                        assertEquals(orderProduct.getAmount(), 1);
                        assertEquals(orderProduct.getPrice(), 1200);
                    }
                    default -> fail("Product not found");
                }
            });

        });
    }
}
