package OMS;

import OMS.data.Database.OrderDatabase;
import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.StockManager.StockManager;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU11 {

    //Daniels test
    static Connection connection = OrderDatabase.getInstance().getConnection();
    static JSONObject testOrder = new JSONObject();
    static int firstOrder;
    static int secondOrder;
    static int thirdOrder;
    static JSONObject firstJSON;
    static JSONObject secondJSON;
    static JSONObject thirdJSON;

    @BeforeAll
    static void setup() {
        //not a smart solution but it works
        try {
            PreparedStatement order_info_stmt = connection.prepareStatement("DELETE FROM order_info WHERE order_id IN (SELECT order_id FROM orders WHERE created > (NOW() - INTERVAL '1 DAY'))");
            PreparedStatement orders_stmt = connection.prepareStatement("DELETE FROM orders WHERE created > (NOW() - INTERVAL '1 DAY')");
            order_info_stmt.execute();
            orders_stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U11.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
            firstJSON = testOrder;
            new CreateOrder().CreateOrder(testOrder);
            firstOrder = new OrderCRUD().getNewestOrderId();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U11.json");
        }

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U11_1.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
            secondJSON = testOrder;
            new CreateOrder().CreateOrder(testOrder);
            secondOrder = new OrderCRUD().getNewestOrderId();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U11_1.json");
        }

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U11_2.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
            thirdJSON = testOrder;
            new CreateOrder().CreateOrder(testOrder);
            thirdOrder = new OrderCRUD().getNewestOrderId();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U11_2.json");
        }

    }

    @Test
    void viewMultipleOrdersAndSpecificTimeFrame() {
        LinkedHashMap<Integer, HashMap<Integer, Integer>> stockReport = new StockManager().getStockReportWithOrders(LocalDate.now(), LocalDate.now());
        assertTrue(stockReport.containsKey(firstOrder));
        assertTrue(stockReport.containsKey(secondOrder));
        assertTrue(stockReport.containsKey(thirdOrder));

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT name FROM orders WHERE id IN (?,?,?)");
            stmt.setInt(1, firstOrder);
            stmt.setInt(2, secondOrder);
            stmt.setInt(3, thirdOrder);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), firstJSON.getJSONObject("customer").getString("name"));
            rs.next();
            assertEquals(rs.getString("name"), secondJSON.getJSONObject("customer").getString("name"));
            rs.next();
            assertEquals(rs.getString("name"), thirdJSON.getJSONObject("customer").getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
