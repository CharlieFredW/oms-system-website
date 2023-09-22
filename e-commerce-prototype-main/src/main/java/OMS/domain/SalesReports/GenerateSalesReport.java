package OMS.domain.SalesReports;

import OMS.domain.OrderCRUD.OrderCRUD;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GenerateSalesReport implements IGetSalesReport {

    OrderCRUD orderCRUD = new OrderCRUD();

    // Creates a sales report as either .txt or .csv file. Adds all orders from "id" to the sales report
    // if no ids are specified, then all orders from the database is added to the sales report.
    public SalesReport generateSalesReport(LocalDate startDate, LocalDate endDate, SalesReportType type, int... id) {
        // Variables
        ArrayList<String> productStrings = new ArrayList<>();
        String separator = "";
        String fileType = "";
        switch (type) {
            case CSV:
                separator = ",";
                fileType = ".csv";
                break;
            case TXT:
                separator = "\t";
                fileType = ".txt";
                break;
        }

        SalesReport salesReport = getOrderInfoByIdAndDate(startDate, endDate, id);

        // If no orders are found return false
        if (salesReport.getTotalOrders() == 0) {
            SalesReport salesReportFail = new SalesReport(salesReport.getTotalPrice(), salesReport.getTotalVat(), salesReport.getTotalOrders(), fileType, false, productStrings);
            return salesReportFail;
        }

        // Write to file
        try {
            FileWriter fileWriter = new FileWriter("src/main/java/OMS/SalesReports/" + startDate + "_" + endDate + fileType);
            fileWriter.append("From" + separator + startDate + "\n");
            fileWriter.append("To" + separator + endDate + "\n");


            // Total price and vat line in the sales report
            fileWriter.append("Total revenue" + separator + salesReport.getTotalPrice() + "\n");
            fileWriter.append("Total vat" + separator + salesReport.getTotalVat() + "\n");
            fileWriter.append("Number of orders" + separator + salesReport.getTotalOrders() + "\n");
            fileWriter.append("Product id" + separator + "Total sold" + separator + "Average price" + separator +
                    "Min price" + separator + "Max price" + separator + "Revenue\n");


            // combine product info in HashMaps for each unique id
            HashMap<Integer, Integer> quantityMap = new HashMap<>();
            HashMap<Integer, Double> totalPriceMap = new HashMap<>();
            HashMap<Integer, Double> priceMap = new HashMap<>();
            HashMap<Integer, Double> minPricesMap = new HashMap<>();
            HashMap<Integer, Double> maxPricesMap = new HashMap<>();
            for (int i = 0; i < salesReport.getProductIdsList().size(); i++) {
                int productId = salesReport.getProductIdsList().get(i);
                int quantity = salesReport.getProductQuantitiesList().get(i);
                double price = salesReport.getProductPricesList().get(i);
                quantityMap.put(productId, quantityMap.getOrDefault(productId, 0) + quantity);
                totalPriceMap.put(productId, totalPriceMap.getOrDefault(productId, 0.0) + price * quantity);
                priceMap.put(productId, price);

                // Save smallest price for productId
                if (minPricesMap.containsKey(productId)) {
                    double minPrice = minPricesMap.get(productId);
                    if (price < minPrice) {
                        minPricesMap.put(productId, price);
                    }
                } else {
                    minPricesMap.put(productId, price);
                }
                // Save biggest price for productId
                if (maxPricesMap.containsKey(productId)) {
                    double maxPrice = maxPricesMap.get(productId);
                    if (price > maxPrice) {
                        maxPricesMap.put(productId, price);
                    }
                } else {
                    maxPricesMap.put(productId, price);
                }
            }

            // Create product lines in Sales Report
            for (int productId : quantityMap.keySet()) {
                String line = productId + separator + quantityMap.get(productId) + separator +
                        totalPriceMap.get(productId) / quantityMap.get(productId) + separator + minPricesMap.get(productId) +
                        separator + maxPricesMap.get(productId) + separator + totalPriceMap.get(productId) + "\n";
                fileWriter.append(line);

                productStrings.add(line);

            }
            fileWriter.close();
            SalesReport salesReportSucces = new SalesReport(salesReport.getTotalPrice(), salesReport.getTotalVat(), salesReport.getTotalOrders(), fileType, true, productStrings);
            return salesReportSucces;
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }


    SalesReport getOrderInfoByIdAndDate(LocalDate startDate, LocalDate endDate, int... id) {
        double totalPrice = 0;
        double totalVat = 0;
        int totalOrders = 0;
        ArrayList<Integer> orderIds = new ArrayList<>();
        ArrayList<Integer> productIdsList = new ArrayList<>();
        ArrayList<Integer> productQuantitiesList = new ArrayList<>();
        ArrayList<Double> productPricesList = new ArrayList<>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);
        Integer[] orderIdsIntegerArray = Arrays.stream(id).boxed().toArray(Integer[]::new);
        Array orderIdsArray = null;
        try {
            orderIdsArray = orderCRUD.getConnection().createArrayOf("integer", orderIdsIntegerArray);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stmt;
            if (id.length == 0) {
                stmt = orderCRUD.getConnection().prepareStatement(
                        "SELECT id, total_price, vat FROM orders WHERE created BETWEEN ? AND ?");
                stmt.setTimestamp(1, Timestamp.valueOf(startDateTime));
                stmt.setTimestamp(2, Timestamp.valueOf(endDateTime));
            } else {
                stmt = orderCRUD.getConnection().prepareStatement(
                        "SELECT id, total_price, vat FROM orders WHERE id = ANY (?) AND created BETWEEN ? AND ?");
                stmt.setArray(1, orderIdsArray);
                stmt.setTimestamp(2, Timestamp.valueOf(startDateTime));
                stmt.setTimestamp(3, Timestamp.valueOf(endDateTime));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orderIds.add(rs.getInt(1));
                totalPrice = totalPrice + rs.getDouble(2); // total_price
                totalVat = totalVat + rs.getDouble(3); // vat
                totalOrders++;
                //System.out.println("price: " + totalPrice + " vat: " + totalVat + " orders: " + totalOrders);
            }

            stmt.close();

        } catch (SQLException e) {
            System.out.println("no orders matched id: " + id + " between " + startDateTime + " and " + endDateTime);
        }
        if (totalOrders > 0) {
            try (PreparedStatement stmt = orderCRUD.getConnection().prepareStatement("SELECT product_id, quantity, price FROM order_info WHERE order_id = ANY (?)")) {
                Integer[] ids = orderIds.toArray(new Integer[orderIds.size()]);
                orderIdsArray = orderCRUD.getConnection().createArrayOf("integer", ids);
                stmt.setArray(1, orderIdsArray);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    productIdsList.add(rs.getInt(1)); // product_id
                    productQuantitiesList.add(rs.getInt(2)); // quantity
                    productPricesList.add(rs.getDouble(3)); // price
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return new SalesReport(totalPrice, totalVat, totalOrders, true, productIdsList, productQuantitiesList, productPricesList);
    }

    public ArrayList<Integer> getAllIds() {
        ArrayList<Integer> orderIds = new ArrayList<>();
        try {
            Statement stmt = orderCRUD.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM orders");
            while (rs.next()) {
                orderIds.add(rs.getInt(1));
            }
            return orderIds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
