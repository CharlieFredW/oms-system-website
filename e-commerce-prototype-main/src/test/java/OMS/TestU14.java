package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.OrderCRUD.OrderCRUD;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestU14 {

    //Nikolajs test for CRUD

    static int orderId;
    CreateOrder createOrder = new CreateOrder();
    JSONObject testOrder;
    OrderCRUD orderCRUD;


    @BeforeEach
    void setUp() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U14.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U14.json");
        }

        createOrder.CreateOrder(testOrder);
        orderId = new OrderCRUD().getNewestOrderId();
        orderCRUD = new OrderCRUD();
    }

    //testing to see if you can create an order
    @Test
    void createOrder() {
        assertEquals(orderId, orderCRUD.getNewestOrderId());

    }

    //testing to see if you can read an order
    @Test
    void readOrder() {
        createOrder.getProductsList().forEach(product -> {
            switch (product.getProductId()) {
                case 1, 2 -> {
                    assertTrue(product.getProductId() == 1 || product.getProductId() == 2);
                    assertEquals(product.getAmount(), 1);
                }
                case 3, 4 -> {
                    assertTrue(product.getProductId() == 3 || product.getProductId() == 4);
                    assertEquals(product.getAmount(), 11);
                }
                default -> fail("Product not found");
            }
        });

    }


    //testing to see if you can update an order
    @Test
    void updateOrder() {

        assertTrue(orderCRUD.updateOrder(423, 123, 1, 1, 69));

    }


    //test that passes if the newest order has been deleted. You can also enter a specific order number you want to delete
    @Test
    void deleteOrder() {
        assertTrue(orderCRUD.deleteOrder(orderCRUD.getNewestOrderId()));

    }


}
