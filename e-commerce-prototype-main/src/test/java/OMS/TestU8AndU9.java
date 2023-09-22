package OMS;

import OMS.data.Database.OrderDatabase;
import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.OrderCRUD.IDatabase;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.SalesReports.GenerateSalesReport;
import OMS.domain.SalesReports.SalesReport;
import OMS.domain.SalesReports.SalesReportType;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestU8AndU9 {

    //Kims test
    private static final IDatabase database = OrderDatabase.getInstance();

    private static final Connection connection = database.getConnection();

    public static Connection getConnection() {
        return connection;
    }

    static JSONObject testOrder;

    static int orderId1;
    static int orderId2;
    static int orderId3;


    @BeforeAll
    static void setup() {

        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/U8.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/U8.json");
        }

        new CreateOrder().CreateOrder(testOrder);
        orderId1 = new OrderCRUD().getNewestOrderId();
        new CreateOrder().CreateOrder(testOrder);
        orderId2 = new OrderCRUD().getNewestOrderId();
        new CreateOrder().CreateOrder(testOrder);
        orderId3 = new OrderCRUD().getNewestOrderId();

        //change created date
        Timestamp date1 = new Timestamp(1672534800000L); //2023-01-01
        Timestamp date2 = new Timestamp(1677632400000L); //2023-03-01
        Timestamp date3 = new Timestamp(1680307200000L); //2023-04-01

        changeCreatedDate(orderId1, date1);
        changeCreatedDate(orderId2, date2);
        changeCreatedDate(orderId3, date3);


    }


    @Test
    void generateSalesReportNoIds() {
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 26);

        assertTrue(gs.generateSalesReport(startDate, endDate, SalesReportType.CSV).getSuccess());
    }

    @Test
    void OnlyOrdersBetweenDates() {
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 28);
        SalesReport salesReport = gs.generateSalesReport(startDate, endDate, SalesReportType.CSV, orderId1, orderId2, orderId3);
        assertEquals(2, salesReport.getTotalOrders());
    }

    @Test
    void fileFormartIsTXT() {
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 29);
        assertEquals(".txt", gs.generateSalesReport(startDate, endDate, SalesReportType.TXT, orderId1, orderId2, orderId3).getFileFormat());
    }

    @Test
    void fileFormartIsCSV() {
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 28);
        assertEquals(".csv", gs.generateSalesReport(startDate, endDate, SalesReportType.CSV, orderId1, orderId2, orderId3).getFileFormat());
    }

    @Test
    void failsIfNoIdsMatch() {
        deleteOrderById(999999999);
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 27);
        assertFalse(gs.generateSalesReport(startDate, endDate, SalesReportType.CSV, 999999999).getSuccess());
    }

    @Test
    void productStringArrayIsEmptyIfFails() {
        deleteOrderById(999999999);
        GenerateSalesReport gs = new GenerateSalesReport();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 28);
        SalesReport salesReport = gs.generateSalesReport(startDate, endDate, SalesReportType.CSV, 999999999);
        assertTrue(salesReport.getProductStrings().isEmpty());
    }


    static void changeCreatedDate(int id, Timestamp date) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET created = ? WHERE id = ?");
            stmt.setTimestamp(1, date);
            stmt.setInt(2, id);
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void deleteOrderById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}