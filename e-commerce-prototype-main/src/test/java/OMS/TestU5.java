package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.Mail;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.OrderPacker.OrderPacker;
import OMS.domain.CreateOrders.OrderStatus;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU5 {

    static int orderId;
    //nikolajs test
    OrderPacker orderPacker;
    CreateOrder createOrder = new CreateOrder();
    Mail mail;

    JSONObject testOrder;

    @BeforeEach
    void setUp() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U5.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U5.json");
        }

        orderPacker = new OrderPacker();
        createOrder.CreateOrder(testOrder);
        orderId = new OrderCRUD().getNewestOrderId();
        mail = new Mail(testOrder.getJSONObject("customer").getString("mail"), "Unit test: U5", "Test");
    }

    // test order after updating it as shipped and sent email
    @Test
    void markOrderAsShipped() {
        assertTrue(orderPacker.updateOrderStatus(orderId, OrderStatus.SHIPPED));
        assertSame(orderPacker.getOrderStatus(orderId), OrderStatus.SHIPPED);
    }

    // test able to send email
    @Test
    void sendOrderConfirmation() {
        assertTrue(mail.sendMail());
    }
}
