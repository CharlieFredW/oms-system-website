package OMS.UI.GUIFramework;

import OMS.domain.StockManager.ISeeStockReport;
import OMS.domain.StockManager.StockManager;
import OMS.domain.StockManager.StockReportFileType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockUIController {

    @FXML
    DatePicker datePickerFrom, datePickerTo;
    @FXML
    TableView<Map.Entry<Integer, Integer>> stockTable, productTable;
    @FXML
    Button csvButton, fileButton, updateProduct, readProductID;
    @FXML
    TextField fileCreated, productId, updateStockT;

    ISeeStockReport stockManager = new StockManager();


    //String path = "/Users/charliewarnes/Desktop/Opgaver i Intellij IDEA/e-commerce-prototype/src/main/java/OMS/stockReports/";
    StockReportFileType type;

    public void initialize() {

        //check if datepicker values are null
        if (datePickerFrom.getValue() == null) {
            datePickerFrom.setValue(LocalDate.now());
        }

        if (datePickerTo.getValue() == null) {
            datePickerTo.setValue(LocalDate.now());
        }

        //Use method from Stock manager to get a linkedHashMap and add it to addLinkedHashMap method to intialize the tableview with data

        LinkedHashMap<Integer, Integer> stockReport = stockManager.generateStockReport(datePickerFrom.getValue(), datePickerTo.getValue());
        addLinkedHashMapToTableView(stockReport);

        //add listener to the datepicker and the new date to the method that tracks stock
        datePickerFrom.setOnAction(event -> {
            LocalDate startDate = datePickerFrom.getValue();
            LocalDate endDate = datePickerTo.getValue();
            LinkedHashMap<Integer, Integer> newStockReport = stockManager.generateStockReport(startDate, endDate);
            addLinkedHashMapToTableView(newStockReport);
        });

        //add listener to the datepicker and the new date to the method that tracks stock
        datePickerTo.setOnAction(event -> {
            LocalDate startDate = datePickerFrom.getValue();
            LocalDate endDate = datePickerTo.getValue();
            LinkedHashMap<Integer, Integer> newStockReport = stockManager.generateStockReport(startDate, endDate);
            addLinkedHashMapToTableView(newStockReport);
        });

        //Add listener to csv button
        csvButton.setOnAction(event -> {
            type = StockReportFileType.CSV;
            stockManager.generateStockReportFile(type, datePickerFrom.getValue(), datePickerTo.getValue());
            fileCreated.setText("Csv file created!");
        });

        //Add listener to file button
        fileButton.setOnAction(event -> {
            type = StockReportFileType.TXT;
            stockManager.generateStockReportFile(type, datePickerFrom.getValue(), datePickerTo.getValue());
            fileCreated.setText("Txt file created!");
        });

        //add listener to updateProduct button
        updateProduct.setOnAction(event -> {
            stockManager.updateStock(Integer.parseInt(productId.getText()), Integer.parseInt(updateStockT.getText()));
            HashMap<Integer, Integer> stock = stockManager.readStock(Integer.parseInt(productId.getText()));
            addReadStockToTableView(stock);
        });

        //Add button listener for reading product id and put into tableview
        readProductID.setOnAction(event -> {
            HashMap<Integer, Integer> product = stockManager.readStock(Integer.parseInt(productId.getText()));
            addReadStockToTableView(product);
        });

    }

    //adds a linkedHashMap to new columns in the tableview
    public void addLinkedHashMapToTableView(LinkedHashMap<Integer, Integer> map) {
        ObservableList<Map.Entry<Integer, Integer>> tableData = FXCollections.observableArrayList(map.entrySet());

        TableColumn<Map.Entry<Integer, Integer>, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setPrefWidth(155);
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey()).asObject());

        TableColumn<Map.Entry<Integer, Integer>, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setPrefWidth(165);
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        stockTable.getColumns().setAll(productIdColumn, quantityColumn);
        stockTable.setItems(tableData);
    }

    //adds a HashMap to new columns in the tableview
    public void addReadStockToTableView(HashMap<Integer, Integer> map) {
        ObservableList<Map.Entry<Integer, Integer>> tableData = FXCollections.observableArrayList(map.entrySet());

        TableColumn<Map.Entry<Integer, Integer>, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setPrefWidth(84);
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey()).asObject());

        TableColumn<Map.Entry<Integer, Integer>, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setPrefWidth(84);
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        productTable.getColumns().setAll(productIdColumn, quantityColumn);
        productTable.setItems(tableData);
    }
}
