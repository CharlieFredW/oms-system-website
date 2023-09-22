package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.OrderPacker.OrderPacker;
import OMS.domain.CreateOrders.OrderStatus;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU3 {

    //kims test
    static JSONObject testOrder;
    OrderPacker orderPacker = new OrderPacker();
    OrderCRUD orderCRUD = new OrderCRUD();
    int orderId = orderCRUD.getNewestOrderId();

    @BeforeAll
    static void setup() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U3.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U3.json");
        }

        new CreateOrder().CreateOrder(testOrder);
    }

    @BeforeEach
    void setUp() {
        System.out.println("Newest order id: " + orderId);
    }


    // test get name
    @Test
    void getName() {
        assertEquals("Name: Mille Peter", orderPacker.getShippingInfo(orderId).get(0));
    }

    // test get address
    @Test
    void getAddress() {
        assertEquals("Address: Millevej 21", orderPacker.getShippingInfo(orderId).get(1));
    }

    // test get city
    @Test
    void getCity() {
        assertEquals("City: Milleby", orderPacker.getShippingInfo(orderId).get(2));
    }

    // test get zip code
    @Test
    void getZipCode() {
        assertEquals("ZipCode: 3829", orderPacker.getShippingInfo(orderId).get(3));
    }

    // test get country
    @Test
    void getCountry() {
        assertEquals("Country: Denmark", orderPacker.getShippingInfo(orderId).get(4));
    }

    // test get delivery method

    // test email notification when order is shipped
    @Test
    void testSendEmailWhenOrderIsShipped() {
        assertTrue(orderPacker.updateOrderStatus(orderId, OrderStatus.SHIPPED));
    }

}
