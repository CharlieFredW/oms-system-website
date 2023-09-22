package OMS.domain.StockManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface ISeeStockReport {

    boolean generateStockReportFile(StockReportFileType filetype, LocalDate startDate, LocalDate endDate, String... path);

    boolean generateStockReportWithOrdersFile(StockReportFileType filetype, LocalDate startDate, LocalDate endDate, boolean duplicateOrderIDs, String... path);

    LinkedHashMap<Integer, Integer> generateStockReport(LocalDate startDate, LocalDate endDate);

    LinkedHashMap<Integer, HashMap<Integer, Integer>> getStockReportWithOrders(LocalDate startDate, LocalDate endDate);

    boolean updateStock(int id, int stock);

    HashMap<Integer, Integer> readStock(int... id);
}
