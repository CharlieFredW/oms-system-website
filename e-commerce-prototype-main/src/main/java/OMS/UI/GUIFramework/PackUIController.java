package OMS.UI.GUIFramework;

import OMS.domain.CreateOrders.Order;
import OMS.domain.OrderPacker.IPackOrders;
import OMS.domain.OrderPacker.OrderPacker;
import OMS.domain.CreateOrders.OrderStatus;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PackUIController implements Initializable {
    @FXML
    public Button selectButton, updateButton, shippingButton;

    @FXML
    public Label numberOfOrdersLabel;

    @FXML
    public SplitMenuButton orderStatusButton;

    @FXML
    public RadioButton processingRadio, processedRadio, packagedRadio, shippedRadio, deliveredRadio, failedRadio;

    @FXML
    public TableView<Order> ordersTableView;

    @FXML
    public TableColumn<Order, Integer> orderNumberColumn = new TableColumn<Order, Integer>();

    @FXML
    public TableColumn<Order, Timestamp> orderDateColumn;

    @FXML
    public TableColumn<Order, Double> priceColumn;

    @FXML
    public TableColumn<Order, OrderStatus> statusColumn;

    @FXML
    public TextArea textArea;

    @FXML
    public TextField idTextField;

    @FXML
    public DatePicker datePickerTo, datePickerFrom;

    @FXML
    public MenuItem processingMenuItem, processedMenuItem, packagedMenuItem, shippedMenuItem,
            deliveredMenuItem, failedMenuItem;


    OrderStatus status, newStatus;
    Integer currentOrderId;
    LocalDate startDate = LocalDate.now().minusYears(1);
    LocalDate endDate = LocalDate.now();
    IPackOrders iPackOrders = new OrderPacker();
    ArrayList<Order> orders = iPackOrders.getOrders(startDate, endDate, false, true, false,
            false, false, false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        processedRadio.setSelected(true);

        // TableView
        orderNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderNumber()).asObject());
        orderDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        ordersTableView.getItems().addAll(orders);
        numberOfOrdersLabel.setText(String.valueOf(orders.size()));

        // Go to an order page if you double-click on an order
        ordersTableView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                goToOrderPage();
            }
        });


        // Go to an order page if you press enter while an order is selected
        ordersTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                goToOrderPage();
            }
        });
        // Go to an order page if you click "select" while an oder is selected
        selectButton.setOnAction(event -> {
            goToOrderPage();
        });


        // Update the TableView of orders
        updateButton.setOnAction(event -> {
            updateTableView();
        });

        // DatePickers
        datePickerFrom.setValue(startDate);
        datePickerFrom.setOnAction(event -> {
            startDate = datePickerFrom.getValue();
        });
        datePickerTo.setValue(endDate);
        datePickerTo.setOnAction(event -> {
            endDate = datePickerTo.getValue();
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
            try {
                currentOrderId = ordersTableView.getSelectionModel().getSelectedItem().getOrderNumber();
            } catch (NullPointerException e) {
                textArea.appendText("Status update failed. Select an order!\n");
                return;
            }
            status = iPackOrders.getOrderStatus(currentOrderId);
            if (!(newStatus == status) && !(newStatus == null)) {
                iPackOrders.updateOrderStatus(currentOrderId, newStatus);
                textArea.appendText("The order status of order: " + currentOrderId + " was updated to: " + newStatus + "\n");
                updateTableView();
            } else if (newStatus == status) {
                textArea.appendText("The order status of order: " + currentOrderId + " is already: " + newStatus + "\n");
            } else if (newStatus == null) {
                textArea.appendText("Select an order status \n");
            }
        });

        // TextFormatter that filters out non-numeric characters
        TextFormatter<Integer> intFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        });
        // TextField for id search, only integers allowed
        idTextField.setTextFormatter(intFormatter);
        idTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateTableView();
            }
        });

        // Shipping info
        shippingButton.setOnAction(event -> {
            try {
                currentOrderId = ordersTableView.getSelectionModel().getSelectedItem().getOrderNumber();
            } catch (NullPointerException e) {
                textArea.appendText("Shipping info failed. Select an order!\n");
                return;
            }
            ArrayList<String> shippingInfo = iPackOrders.getShippingInfo(currentOrderId);
            for (String line : shippingInfo) {
                textArea.appendText(line + "\n");
            }
        });

    } // end of initialize

    private void goToOrderPage() {
        Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Open new order page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/OMS_UI/OrderPage.fxml"));
                Scene scene = new Scene(loader.load(), 500, 600);
                Stage stage = new Stage();

                // Pass order id to new controller
                OrderPageController controller = loader.getController();
                controller.setOrderId(selectedOrder.getOrderNumber());

                stage.setScene(scene);
                stage.setTitle("Order number: " + selectedOrder.getOrderNumber());
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    void updateTableView() {
        if (idTextField.getText().equals("")) {
            orders = iPackOrders.getOrders(startDate, endDate, processingRadio.isSelected(), processedRadio.isSelected(),
                    packagedRadio.isSelected(), shippedRadio.isSelected(), deliveredRadio.isSelected(), failedRadio.isSelected());
        } else {
            int orderNumbers = Integer.parseInt(idTextField.getText());
            orders = iPackOrders.getOrders(startDate, endDate, processingRadio.isSelected(), processedRadio.isSelected(),
                    packagedRadio.isSelected(), shippedRadio.isSelected(), deliveredRadio.isSelected(), failedRadio.isSelected(), orderNumbers);
        }

        ordersTableView.getItems().clear();
        ordersTableView.getItems().addAll(orders);
        numberOfOrdersLabel.setText(String.valueOf(orders.size()));
        textArea.appendText("Order list updated\n");
    }

}