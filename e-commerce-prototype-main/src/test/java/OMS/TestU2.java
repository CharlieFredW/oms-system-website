package OMS;

import OMS.domain.CreateOrders.CreateOrder;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU2 {

    //chalies test

    static OrderPacker orderPacker;
    static CreateOrder createOrder = new CreateOrder();
    static OrderCRUD orderCRUD;
    static JSONObject testOrder;

    @BeforeAll
    static void setUp() {
        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U2.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U2.json");
        }


        orderPacker = new OrderPacker();
        createOrder.CreateOrder(testOrder);
        orderCRUD = new OrderCRUD();

    }

    // test update order status and email notification
    @Test
    void testUpdateOrderAndEmailNotification() {
        assertTrue(orderPacker.updateOrderStatus(orderCRUD.getNewestOrderId(), OrderStatus.PROCESSED));
    }

    // test send order confirmation
    @Test
    void testSendOrderConfirmation() {
        assertTrue(orderCRUD.createOrder(createOrder));
    }

    // test send notifcation to courier
    @Test
    void testSendNotificationToCourier() {
        assertTrue(orderPacker.sendNotificationToCourier(orderCRUD.getNewestOrderId()));
    }

}
