package OMS.UI.GUIFramework;

import OMS.domain.CreateOrders.OrderProducts;
import OMS.domain.OrderCRUD.OrderCRUD;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageControllerCustomer implements Initializable {

    @FXML
    public Label orderNumberLabel, totalProductsLabel;

    @FXML
    public TableView<OrderProducts> productsTableView;

    @FXML
    public TableColumn<OrderProducts, Double> priceColumn;

    @FXML
    public TableColumn<OrderProducts, Integer> productIdColumn;

    @FXML
    public TableColumn<OrderProducts, Integer> quantityColumn;

    int orderId, totalProducts;
    ArrayList<OrderProducts> products;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // adding values to cells
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());

        // Have to run later, because the order id is passed to the controller after initialize() is run.
        Platform.runLater(() -> {
            products = getProducts(orderId);
            productsTableView.getItems().addAll(products);
            for (OrderProducts orderProducts : products) {
                totalProducts = totalProducts + orderProducts.getAmount();
            }
            totalProductsLabel.setText(String.valueOf(totalProducts));
            orderNumberLabel.setText(String.valueOf(orderId));
        });


    }

    //method to get products by order id and put them into an arraylist
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
