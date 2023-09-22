package OMS;

import OMS.data.Database.OrderDatabase;
import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.StockManager.StockManager;
import OMS.domain.StockManager.StockReportFileType;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestU10 {

    //Daniels test
    static JSONObject testOrder = new JSONObject();
    static int numberOfOrders = 3;

    @BeforeAll
    static void setup() {
        //not a smart solution but it works
        try {
            PreparedStatement order_info_stmt = OrderDatabase.getInstance().getConnection().prepareStatement("DELETE FROM order_info WHERE order_id IN (SELECT order_id FROM orders WHERE created > (NOW() - INTERVAL '1 DAY'))");
            PreparedStatement orders_stmt = OrderDatabase.getInstance().getConnection().prepareStatement("DELETE FROM orders WHERE created > (NOW() - INTERVAL '1 DAY')");
            order_info_stmt.execute();
            orders_stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U10.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U10.json");
        }

        for (int i = 0; i < numberOfOrders; i++) {
            new CreateOrder().CreateOrder(testOrder);
        }
    }

    @Test
    void testGenerateStockReportFile() throws FileNotFoundException {
        //gets the absolute path for the test class so that the csv file can be created in the same folder as the test class
        String path = this.getClass().getResource("").getPath();
        String absolutePath = new File(path).getAbsolutePath() + "\\";

        //creates the file
        new StockManager().generateStockReportFile(StockReportFileType.CSV, LocalDate.now(), LocalDate.now(), absolutePath);

        //calculates the total products sold and the total price of the products sold from the JSON object / JSON file
        AtomicInteger sum = new AtomicInteger();
        AtomicInteger totalProducts = new AtomicInteger();
        HashMap<Integer, Integer> products = new HashMap<>();
        testOrder.getJSONObject("cart").keySet().forEach(key -> {
            int id = Integer.parseInt(testOrder.getJSONObject("cart").getJSONObject(key).get("id").toString());
            int quantity = Integer.parseInt(testOrder.getJSONObject("cart").getJSONObject(key).get("amount").toString());
            totalProducts.addAndGet(quantity);
            int price = Integer.parseInt(testOrder.getJSONObject("cart").getJSONObject(key).get("price").toString());
            sum.addAndGet(quantity * price);
            products.put(id, quantity);
        });
        sum.set(sum.get() * numberOfOrders);
        totalProducts.set(totalProducts.get() * numberOfOrders);
        products.forEach((id, quantity) -> products.replace(id, quantity * numberOfOrders));

        //loads the csv file that was just created with the method generateStockReportFile
        Scanner scanner = new Scanner(new File(absolutePath + LocalDate.now() + "_" + LocalDate.now() + ".csv"));
        for (int i = 0; i < 2; i++) {
            //skip two lines (from and to dates)
            scanner.nextLine();
        }

        //gets the total products sold from the csv
        int totalProductsInFile = Integer.parseInt(scanner.nextLine().split(",")[1]);
        assertEquals(totalProductsInFile, totalProducts.get());

        //skip one line
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            int id = Integer.parseInt(line[0]);
            int quantity = Integer.parseInt(line[1]);
            assertEquals(products.get(id), quantity);
        }
    }

    @Test
    void testGenerateStockReportWithOrdersFile() {

    }
}
