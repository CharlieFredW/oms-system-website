package OMS;

import OMS.data.Database.OrderDatabase;
import OMS.domain.StockManager.StockManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestU12 {

    //daniel test
    static Connection connection = OrderDatabase.getInstance().getConnection();
    static int firstProductID;
    static int secondProductID;
    static int thirdProductID;

    @BeforeAll
    static void setup() {
        //create a product for testing
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO products (stock, title) " +
                    "VALUES (10,'Test Product'), " +
                    "(25,'Test Product 2'), " +
                    "(69,'Test Product 3') returning id");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            firstProductID = rs.getInt("id");
            rs.next();
            secondProductID = rs.getInt("id");
            rs.next();
            thirdProductID = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void teardown() {
        //delete the product
        try {
            connection.prepareStatement("DELETE FROM products WHERE title IN ('Test Product','Test Product 2','Test Product 3')").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readStockForProductID() {
        HashMap<Integer, Integer> allProductStocks = new StockManager().readStock();
        assertTrue(allProductStocks.size() > 2);

        HashMap<Integer, Integer> specificProductStocks = new StockManager().readStock(firstProductID, thirdProductID);
        assertEquals(2, specificProductStocks.size());

        assertTrue(specificProductStocks.containsKey(firstProductID));
        assertTrue(specificProductStocks.containsKey(thirdProductID));

        assertEquals(10, specificProductStocks.get(firstProductID));
        assertEquals(69, specificProductStocks.get(thirdProductID));
    }

    @Test
    void updateStockForProductID() {
        new StockManager().updateStock(firstProductID, 5);
        assertEquals(5, new StockManager().readStock(firstProductID).get(firstProductID));

        new StockManager().updateStock(firstProductID, 420);
        assertEquals(420, new StockManager().readStock(firstProductID).get(firstProductID));

        new StockManager().updateStock(firstProductID, 69);
        assertEquals(69, new StockManager().readStock(firstProductID).get(firstProductID));
    }
}
