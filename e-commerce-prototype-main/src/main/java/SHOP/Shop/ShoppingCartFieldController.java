package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import SHOP.Logic.Product;
import SHOP.Logic.ShoppingCart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Map;
import java.text.DecimalFormat;

public class ShoppingCartFieldController implements Controller {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @FXML
    private VBox    shoppingCartProducts;
    @FXML
    private Label   totalLabel,
            vatLabel,
            totalTextLabel,
            vatTextLabel;
    @FXML
    private Button  checkoutButton,
            clearShoppingCartBtn;

    public void showShoppingCart(MainController mainController,
                                 VBox           shoppingCartProducts) {
        if (mainController.getShoppingCart().getCart().isEmpty()) {
            // hiding shopping cart buttons and labels
            emptyShoppingCart(mainController);
        } else {
            for (Map.Entry<Product, Integer> entry : mainController.getShoppingCart().getCart().entrySet()) {
                try {
                    CMSHandler cmsHandler1 = new CMSHandler();
                    FXMLLoader productLoader = cmsHandler1.getPage("shopping_cart_product");
                    HBox product = productLoader.load();

                    ShoppingCartDataController shoppingCartDataController = productLoader.getController();
                    shoppingCartDataController.updateShoppingCartItems(entry);
                    product.setPadding(new Insets(10, 0, 0, 0));
                    product.setId(entry.getKey().getID());
                    shoppingCartProducts.getChildren().add(product);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            updateTotalPriceLabel(mainController.getShoppingCart());
            updateVATLabel(mainController.getShoppingCart());
            shoppingCartProducts.setAlignment(Pos.CENTER);
            checkoutButton.setOnMouseClicked(mouseEvent -> mainController.getCheckoutController().checkoutLogin());
            clearShoppingCartBtn.setOnMouseClicked(mouseEvent -> {
                mainController.getShoppingCart().clearCart();
                shoppingCartProducts.getChildren().clear();
                emptyShoppingCart(mainController);
            });
        }
    }

    public void updateTotalPriceLabel(ShoppingCart shoppingCart){
        totalLabel.setText("DKK" + df.format(shoppingCart.getTotalPrice()) + ",-");
    }
    public void updateVATLabel(ShoppingCart shoppingCart){
        vatLabel.setText("DKK" + df.format(shoppingCart.calculateVAT()) + ",-");
    }
    public VBox getShoppingCartProducts() {
        return shoppingCartProducts;
    }

    public void emptyShoppingCart(MainController mainController){
        checkoutButton.setVisible(false);
        clearShoppingCartBtn.setVisible(false);
        totalTextLabel.setVisible(false);
        totalLabel.setVisible(false);
        vatLabel.setVisible(false);
        vatTextLabel.setVisible(false);

        // adding label with link to home page
        Label goShoppingLabel = new Label("Your shopping cart is empty\n" +
                "Click here to browser our products");
        goShoppingLabel.setTextFill(Color.BLUE);
        goShoppingLabel.setOnMouseClicked(mouseEvent -> mainController.showHomePage());
        shoppingCartProducts.getChildren().add(goShoppingLabel);
    }
}