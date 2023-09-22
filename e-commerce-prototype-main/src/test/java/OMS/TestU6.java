package OMS;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.CreateOrders.OrderStatus;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class TestU6 {

    static JSONObject testOrder;

    //Daniels test

    @BeforeAll
    static void setup() {
        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U6.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U6.json");
        }

        new CreateOrder().CreateOrder(testOrder);
    }

    // get current orders means orders that are still in the process of being delivered
    @Test
    void getCurrentOrders() {
        new OrderCRUD().getCurrentOrders().forEach(order -> {
            assertNotSame(order.getStatus(), OrderStatus.DELIVERED);
        });
    }
}
