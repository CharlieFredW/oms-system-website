package OMS.domain.StockManager;

import OMS.UI.OtherGroupsAPIS.IUpdateStock;
import OMS.domain.OrderCRUD.OrderCRUD;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockManager implements ISeeStockReport, IUpdateStock {

    private final Connection connection = new OrderCRUD().getConnection();

    public boolean generateStockReportFile(StockReportFileType filetype, LocalDate startDate, LocalDate endDate, String... path) {
        LinkedHashMap<Integer, Integer> stockReport = generateStockReport(startDate, endDate);
        if (stockReport.isEmpty()) {
            throw new NullPointerException("No orders found in the given time period");
        }
        String filename = (path.length == 0 ? "src/main/java/OMS/StockReports/" : path[0]) + startDate + "_" + endDate + ".";
        int totalProductsSold = stockReport.values().stream().mapToInt(Integer::intValue).sum();
        try (FileWriter fileWriter = new FileWriter(filename + filetype.toString().toLowerCase())) {
            switch (filetype) {
                case CSV -> {
                    char separator = ',';
                    fileWriter.append("From" + separator + startDate + "\n");
                    fileWriter.append("To" + separator + endDate + "\n");
                    fileWriter.append("Total products sold" + separator + totalProductsSold + "\n");
                    fileWriter.append("Product ID" + separator + "Quantity\n");

                    for (int productId : stockReport.keySet()) {
                        System.out.println(productId);
                        fileWriter.append(String.valueOf(productId) + separator + stockReport.get(productId) + "\n");
                    }
                }

                case TXT -> {
                    fileWriter.append("From: " + startDate + "\n");
                    fileWriter.append("To: " + endDate + "\n");
                    fileWriter.append("Total products sold: " + totalProductsSold + "\n");
                    fileWriter.append("Product ID: Quantity\n");

                    for (int productId : stockReport.keySet()) {
                        fileWriter.append(productId + ": " + stockReport.get(productId) + "\n");
                    }
                }
                default -> {
                    throw new IllegalArgumentException("Invalid file type");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public LinkedHashMap<Integer, Integer> generateStockReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        LinkedHashMap<Integer, Integer> stockReport = new LinkedHashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT order_info.product_id, SUM(quantity) "
                + "FROM order_info LEFT JOIN orders ON orders.id = order_info.order_id "
                + "WHERE orders.created BETWEEN ? AND ? "
                + "GROUP BY order_info.product_id "
                + "ORDER BY SUM(order_info.quantity) DESC")) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTime));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stockReport.put(rs.getInt(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DateTimeException e) {
            System.out.println("Invalid date");
        }
        return stockReport;
    }

    public boolean generateStockReportWithOrdersFile(StockReportFileType filetype, LocalDate startDate, LocalDate endDate, boolean duplicateOrderIDs, String... path) {
        LinkedHashMap<Integer, HashMap<Integer, Integer>> stockReport = getStockReportWithOrders(startDate, endDate);
        if (stockReport.isEmpty()) {
            throw new NullPointerException("No orders found in the given time period");
        }
        String filename = (path.length == 0 ? "src/main/java/OMS/StockReports/" : path[0]) + "WithOrders_" + startDate + "_" + endDate + ".";
        int totalProductsSold = stockReport.values().stream().mapToInt(returnMap -> returnMap.values().stream().mapToInt(Integer::intValue).sum()).sum();
        int avgProductsSold = totalProductsSold / stockReport.size();
        try (FileWriter fileWriter = new FileWriter(filename + filetype.toString().toLowerCase())) {
            switch (filetype) {
                case CSV -> {
                    char separator = ',';
                    fileWriter.append("From" + separator + startDate + "\n");
                    fileWriter.append("To" + separator + endDate + "\n");
                    fileWriter.append("Total products sold" + separator + totalProductsSold + "\n");
                    fileWriter.append("Average products sold per order" + separator + avgProductsSold + "\n");
                    fileWriter.append("Order ID" + separator + "Product ID" + separator + "Quantity\n");

                    for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : stockReport.entrySet()) {
                        int i = 0;
                        for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
                            if (i != 0 && !duplicateOrderIDs) {
                                fileWriter.append(separator + String.valueOf(entry2.getKey()) + separator + entry2.getValue() + "\n");
                            } else {
                                fileWriter.append(String.valueOf(entry.getKey()) + separator + entry2.getKey() + separator + entry2.getValue() + "\n");
                            }
                            i++;
                        }
                    }

                }
                case TXT -> {
                    fileWriter.append("From: " + startDate + "\n");
                    fileWriter.append("To: " + endDate + "\n");
                    fileWriter.append("Total products sold: " + totalProductsSold + "\n");
                    fileWriter.append("Average products sold per order: " + avgProductsSold + "\n");
                    fileWriter.append("Order ID: Product ID: Quantity\n");

                    if (!duplicateOrderIDs) {
                        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : stockReport.entrySet()) {
                            fileWriter.append(entry.getKey() + "-----------------------------------------\n");
                            for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
                                fileWriter.append("\t\t\t" + entry2.getKey() + ": " + entry2.getValue() + "\n");
                            }
                        }
                    } else {
                        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : stockReport.entrySet()) {
                            for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
                                fileWriter.append(entry.getKey() + ": " + entry2.getKey() + ": " + entry2.getValue() + "\n");
                            }
                        }
                    }

                }
                default -> throw new IllegalArgumentException("Invalid file type");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public LinkedHashMap<Integer, HashMap<Integer, Integer>> getStockReportWithOrders(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        LinkedHashMap<Integer, HashMap<Integer, Integer>> stockReport = new LinkedHashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT order_info.order_id, order_info.product_id, order_info.quantity "
                + "FROM order_info LEFT JOIN orders ON orders.id = order_info.order_id "
                + "WHERE orders.created BETWEEN ? AND ? ORDER BY orders.created DESC")) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTime));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (stockReport.containsKey(rs.getInt(1))) {
                    stockReport.get(rs.getInt(1)).put(rs.getInt(2), rs.getInt(3));
                } else {
                    stockReport.put(rs.getInt(1), new LinkedHashMap<>() {
                        {
                            put(rs.getInt(2), rs.getInt(3));
                        }
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockReport;
    }

    public boolean deleteStock(int... id) {
        if (id.length != 0) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM products WHERE id IN (?" + ",?".repeat(id.length - 1) + ")")) {
                for (int i = 0; i < id.length; i++) {
                    stmt.setInt(i + 1, id[i]);
                }
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean updateStock(int id, int stock) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE products SET stock = ? WHERE id = ?")) {
            stmt.setInt(1, stock);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<Integer, Integer> readStock(int... id) {
        HashMap<Integer, Integer> stock = new HashMap<>();
        if (id.length == 0) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT id, stock FROM products ORDER BY id DESC")) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    stock.put(rs.getInt(1), rs.getInt(2));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT id, stock FROM products WHERE id IN (?" + ",?".repeat(id.length - 1) + ") ORDER BY id DESC")) {
                for (int i = 0; i < id.length; i++) {
                    stmt.setInt(i + 1, id[i]);
                }
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    stock.put(rs.getInt(1), rs.getInt(2));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stock;
    }
}
