package PIM.Database;

import PIM.domain.PersistenceHandler;
import PIM.domain.Product;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    List<Product> productsAdded = new ArrayList<>();
    private void addTestProducts() {
        for (int i = 0; i < 500; i++) {
            String name = "productTest" + i;
            Product product = new Product(name, "blabla", "color", 500, "category", 200, 200);
            PersistenceHandler.getInstance().addProduct(product);
            productsAdded.add(product);
        }
    }
    private void removeTestProducts() {
        for (Product product : productsAdded) {
            PersistenceHandler.getInstance().deleteProduct(product);
        }
    }
    @Test
    public void DatabaseIsConnected() {
        try {
            assertTrue(PersistenceHandler.getInstance().getConnection().isValid(3));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void FetchListOfProducts() {
        List<Product> results = PersistenceHandler.getInstance().findAllProducts();
        results.forEach((e) -> {
            System.out.println(e.getName());
        });
        assertNotNull(PersistenceHandler.getInstance().findAllProducts());
    }

    @Test
    public void searchProductsByNameTestPerfect() {
        addTestProducts();
        long startTime = System.nanoTime();
        PersistenceHandler.getInstance().searchProductsByName("Iphone 4", 0);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(duration);
        removeTestProducts();
        assertTrue(duration <= 1000);
    }
    @Test
    public void searchProductsByNameTestImperfect() {
        addTestProducts();
        long startTime = System.nanoTime();
        PersistenceHandler.getInstance().searchProductsByName("productTest", 3);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(duration);
        removeTestProducts();
        assertTrue(duration <= 1000);

    }
    @Test
    public void databaseCapacityTest() {

        try {
            addTestProducts();
            // Check the capacity here
            assertTrue((PersistenceHandler.getInstance().findAllProducts().size()) > 500);
            //assertEquals("productTest77",PersistenceHandler.getInstance().searchProductsByName("productTest77",0));
        } finally {
            // Delete all added products
            removeTestProducts();
        }
    }
}
