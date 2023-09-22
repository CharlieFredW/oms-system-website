package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.Mail;
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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU4 {

    //nikolaj test
    static int orderId;
    static OrderPacker orderPacker;
    static CreateOrder createOrder = new CreateOrder();
    static Mail mail;

    static JSONObject testOrder;

    @BeforeAll
    static void setUp() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U4.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U4.json");
        }

        orderPacker = new OrderPacker();
        createOrder.CreateOrder(testOrder);
        orderId = new OrderCRUD().getNewestOrderId();
        mail = new Mail(testOrder.getJSONObject("customer").getString("mail"), "Unit test: U5", "Test");
    }

    // test able to send email
    @Test
    void sendOrderConfirmation() {
        assertTrue(mail.sendMail());
    }

    // test order after updating it as processed and sent email
    @Test
    void markOrderAsProcessed() {
        assertTrue(orderPacker.updateOrderStatus(orderId, OrderStatus.PROCESSED));
        assertSame(orderPacker.getOrderStatus(orderId), OrderStatus.PROCESSED);
    }


}
