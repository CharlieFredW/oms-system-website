package OMS.domain.SalesReports;

import java.util.ArrayList;

public class SalesReport {
    private final double totalPrice;
    private final double totalVat;
    private final int totalOrders;
    private String fileFormat;
    private final boolean success;
    private ArrayList<String> productStrings;
    private ArrayList<Integer> productIdsList = new ArrayList<>();
    private ArrayList<Integer> productQuantitiesList = new ArrayList<>();
    private ArrayList<Double> productPricesList = new ArrayList<>();


    public SalesReport(double totalPrice, double totalVat, int totalOrders, String fileFormat, boolean success, ArrayList<String> productStrings) {
        this.totalPrice = totalPrice;
        this.totalVat = totalVat;
        this.totalOrders = totalOrders;
        this.fileFormat = fileFormat;
        this.success = success;
        this.productStrings = productStrings;

    }

    public SalesReport(double totalPrice, double totalVat, int totalOrders, boolean success,
                       ArrayList<Integer> productIdsList, ArrayList<Integer> productQuantitiesList,
                       ArrayList<Double> productPricesList) {
        this.totalPrice = totalPrice;
        this.totalVat = totalVat;
        this.totalOrders = totalOrders;
        this.success = success;
        this.productIdsList = productIdsList;
        this.productQuantitiesList = productQuantitiesList;
        this.productPricesList = productPricesList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalVat() {
        return totalVat;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public boolean getSuccess() {
        return success;
    }

    public ArrayList<String> getProductStrings() {
        return productStrings;
    }

    public ArrayList<Integer> getProductIdsList() {
        return productIdsList;
    }

    public ArrayList<Integer> getProductQuantitiesList() {
        return productQuantitiesList;
    }

    public ArrayList<Double> getProductPricesList() {
        return productPricesList;
    }

}
