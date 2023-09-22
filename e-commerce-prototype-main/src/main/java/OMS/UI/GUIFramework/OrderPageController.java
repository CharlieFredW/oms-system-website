package OMS.UI.GUIFramework;

import OMS.domain.CreateOrders.OrderProducts;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.OrderPacker.IPackOrders;
import OMS.domain.OrderPacker.OrderPacker;
import OMS.domain.CreateOrders.OrderStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    @FXML
    public Label orderNumberLabel, totalProductsLabel, orderStatusLabel;

    @FXML
    public Button printButton, packedButton, shippingButton;

    @FXML
    public TextArea textArea;

    @FXML
    public SplitMenuButton orderStatusButton;

    @FXML
    public TableView<OrderProducts> productsTableView;

    @FXML
    public TableColumn<OrderProducts, Double> priceColumn;

    @FXML
    public TableColumn<OrderProducts, Integer> productIdColumn;

    @FXML
    public TableColumn<OrderProducts, Integer> quantityColumn;

    @FXML
    public MenuItem processingMenuItem, processedMenuItem, packagedMenuItem, shippedMenuItem,
            deliveredMenuItem, failedMenuItem;

    IPackOrders orderPacker = new OrderPacker();
    int orderId, totalProducts;
    OrderStatus status, newStatus;
    ArrayList<OrderProducts> products;


    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());


        printButton.setOnAction(event -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(printButton.getScene().getWindow())) {
                boolean success = job.printPage(printButton.getScene().getRoot());
                if (success) {
                    job.endJob();
                    textArea.appendText("Print was successful");
                }
            }
        });

        packedButton.setOnAction(event -> {
            status = orderPacker.getOrderStatus(orderId);
            if (status.equals(OrderStatus.PROCESSED)) {
                orderPacker.updateOrderStatus(orderId, OrderStatus.PACKAGED);
                textArea.appendText("Order: " + orderId + " has been marked as 'packaged'\n");
            } else {
                textArea.appendText("Error: Order: " + orderId + " has already been marked as 'packaged'\n");
            }
        });

        shippingButton.setOnAction(event -> {
            ArrayList<String> shippingInfo = orderPacker.getShippingInfo(orderId);
            for (String line : shippingInfo) {
                textArea.appendText(line + "\n");
            }

        });

        // Initialize MenuItems for SplitMenuButton
        processingMenuItem = new MenuItem("Processing");
        processedMenuItem = new MenuItem("Processed");
        packagedMenuItem = new MenuItem("Packaged");
        shippedMenuItem = new MenuItem("Shipped");
        deliveredMenuItem = new MenuItem("Delivered");
        failedMenuItem = new MenuItem("Failed");
        processingMenuItem.setOnAction(statusHandler);
        processedMenuItem.setOnAction(statusHandler);
        packagedMenuItem.setOnAction(statusHandler);
        shippedMenuItem.setOnAction(statusHandler);
        deliveredMenuItem.setOnAction(statusHandler);
        failedMenuItem.setOnAction(statusHandler);

        orderStatusButton.getItems().addAll(processingMenuItem, processedMenuItem, packagedMenuItem, shippedMenuItem,
                deliveredMenuItem, failedMenuItem);

        // Update order status
        orderStatusButton.setOnAction(event -> {
            status = orderPacker.getOrderStatus(orderId);
            if (!(newStatus == status) && !(newStatus == null)) {
                orderPacker.updateOrderStatus(orderId, newStatus);
                textArea.appendText("The order status updated to: " + newStatus + "\n");
                orderStatusLabel.setText(String.valueOf(newStatus));
                status = newStatus;
            } else if (newStatus == status) {
                textArea.appendText("The order status is already: " + newStatus + "\n");
            } else if (newStatus == null) {
                textArea.appendText("Select an order status \n");
            }
        });


        // Have to run later, because the order id is passed to the controller after initialize() is run.
        Platform.runLater(() -> {
            products = getProducts(orderId);
            productsTableView.getItems().addAll(products);
            for (OrderProducts orderProducts : products) {
                totalProducts = totalProducts + orderProducts.getAmount();
            }
            totalProductsLabel.setText(String.valueOf(totalProducts));
            orderNumberLabel.setText(String.valueOf(orderId));
            status = orderPacker.getOrderStatus(orderId);
            orderStatusLabel.setText(String.valueOf(status));
        });

    }

    EventHandler<ActionEvent> statusHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {

            Object source = event.getSource();
            if (source == processingMenuItem) {
                orderStatusButton.setText(processingMenuItem.getText());
                newStatus = OrderStatus.PROCESSING;
            } else if (source == processedMenuItem) {
                orderStatusButton.setText(processedMenuItem.getText());
                newStatus = OrderStatus.PROCESSED;
            } else if (source == packagedMenuItem) {
                orderStatusButton.setText(packagedMenuItem.getText());
                newStatus = OrderStatus.PACKAGED;
            } else if (source == shippedMenuItem) {
                orderStatusButton.setText(shippedMenuItem.getText());
                newStatus = OrderStatus.SHIPPED;
            } else if (source == deliveredMenuItem) {
                orderStatusButton.setText(deliveredMenuItem.getText());
                newStatus = OrderStatus.DELIVERED;
            } else if (source == failedMenuItem) {
                orderStatusButton.setText(failedMenuItem.getText());
                newStatus = OrderStatus.FAILED;
            }
        }
    };


    public ArrayList<OrderProducts> getProducts(int orderId) {
        ArrayList<OrderProducts> products = new ArrayList<>();
        try {
            OrderCRUD orderCRUD = new OrderCRUD();
            PreparedStatement stmt = orderCRUD.getConnection().prepareStatement("SELECT * FROM order_info WHERE order_id = ?");
            stmt.setInt(1, orderId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");


                products.add(new OrderProducts(id, quantity, price));
                //System.out.println(products);
            }

            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
