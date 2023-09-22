package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.Customer.Customer;
import OMS.domain.OrderCRUD.OrderCRUD;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestU1 {

    //daniels test

    static CreateOrder createOrder = new CreateOrder();
    static JSONObject testOrder;
    static int orderId;

    @BeforeAll
    static void setUp() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U1.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U1.json");
        }
        createOrder.CreateOrder(testOrder);
        orderId = new OrderCRUD().getNewestOrderId();
    }

    // Get a list items (id, quantity & title) from an order
    @Test
    void getListOfItemsFromOrder() {
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

    // Get a list of items (id, quantity & title) from an order by ID
    @Test
    void getListOfItemsFromOrderByID() {
        new Customer().getProductsInOrder(orderId).forEach(product -> {
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
}
