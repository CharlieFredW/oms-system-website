package OMS.UI.GUIFramework;

import OMS.domain.CreateOrders.CreateOrder;
import OMS.domain.CreateOrders.Order;
import OMS.domain.OrderCRUD.IOrderCRUD;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.CreateOrders.OrderStatus;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderCRUDUIController implements Initializable {
    @FXML
    public Button Search, Create, Delete;

    @FXML
    public TextField TextFieldID;

    @FXML
    public TableView<Order> TableViewOrders;

    @FXML
    public TableColumn<Order, Integer> ColumnOrderID = new TableColumn<Order, Integer>();

    @FXML
    public TableColumn<Order, Double> ColumnPrice;

    @FXML
    public TableColumn<Order, OrderStatus> ColumnStatus;

    JSONObject testOrder;

    Integer orderID;

    IOrderCRUD iOrderCRUD = new OrderCRUD();

    ArrayList<Order> orders = iOrderCRUD.getOrdersCRUD();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // adding values to cells
        ColumnOrderID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderNumber()).asObject());
        ColumnPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        ColumnStatus.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        TableViewOrders.getItems().addAll(orders);

        // if you click on an order you will go to order page where you can see the order products
        TableViewOrders.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                goToOrderPage();
            }
        });

        TextFormatter<Integer> intFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        });
        // TextField for id search, only integers allowed
        TextFieldID.setTextFormatter(intFormatter);
        TextFieldID.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateTableView();
                System.out.println(orderID);
            }
        });

        // create a new order when you press the create button
        try {
            File testOrder_file = new File("src/test/java/OMS/jsonTestOrders/blankOrderCRUD.json");
            InputStream test_is = new FileInputStream(testOrder_file);
            testOrder = new JSONObject(new JSONTokener(test_is));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file: " + "src/test/java/OMS/jsonTestOrders/blankOrderCrud.json");
        }


        Create.setOnAction(event -> {
            new CreateOrder().CreateOrder(testOrder);
        });

        // delete an order when you press the delete button
        Delete.setOnAction(event -> {
            orderID = Integer.parseInt(TextFieldID.getText());
            iOrderCRUD.deleteOrder(orderID);
            updateTableView();
            System.out.println(orderID);
        });

        Search.setOnAction(event -> {
            updateTableView();
        });

    }

    //method to go to order page
    private void goToOrderPage() {
        Order selectedOrder = TableViewOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/OMS_UI/OrderPageCRUD.fxml"));
                Scene scene = new Scene(loader.load(), 480, 500);
                Stage stage = new Stage();

                OrderPageControllerCRUD controller = loader.getController();
                controller.setOrderId(selectedOrder.getOrderNumber());

                stage.setScene(scene);
                stage.setTitle("Order viewer");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to update the tableview
    void updateTableView() {
        if (TextFieldID.getText().equals("")) {
            orders = iOrderCRUD.getOrdersCRUD();
            System.out.println("fejl 1");
        } else {
            int orderNumbers = Integer.parseInt(TextFieldID.getText());
            orders = iOrderCRUD.getOrdersCRUD(orderNumbers);
            System.out.println("fejl 2");
        }
        TableViewOrders.getItems().clear();
        TableViewOrders.getItems().addAll(orders);
        System.out.println("fejl 3");
    }
}


