package SHOP.Shop;

import SHOP.Logic.Product;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartDataController {
    @FXML
    private ImageView   productImageView;
    @FXML
    private Label       productTitleLabel,
            productDescriptionLabel,
            productPriceLabel,
            productAmountLabel,
            minusOneProductLabel,
            plusOneProductLabel,
            removeProductLabel;

    private MainController mainController = MainController.getInstance();

    public void updateShoppingCartItems(Map.Entry<Product,Integer>  entry){

        // setting product image
        productImageView.setImage(new Image
                (new File(entry.getKey().getImageDirectory()).toURI().toString()));
        // setting product title
        productTitleLabel.setText(entry.getKey().getTitle());
        productTitleLabel.setStyle("-fx-font-weight: bold");
        // setting product description
        productDescriptionLabel.setText(entry.getKey().getDescription());
        // setting product price
        productPriceLabel.setText(entry.getKey().getPrice()+",-");
        // setting product amount
        productAmountLabel.setText(entry.getValue().toString());
        // setting minusOneProductLabel action on specific product
        minusOneProductLabel.setOnMouseClicked(mouseEvent -> {
            mainController.getShoppingCart().decrementItem(entry.getKey().getID());
            if(entry.getValue() <= 0){
                removeProduct(entry, mainController);
            }
            updateShoppingCartItems(entry);
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateTotalPriceLabel(mainController.getShoppingCart());
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateVATLabel(mainController.getShoppingCart());
            mainController.getShoppingCartController().showShoppingCart();

        });
        // setting plusOneProductLabel action on specific product
        plusOneProductLabel.setOnMouseClicked(mouseEvent -> {
            mainController.getShoppingCart().incrementItem(entry.getKey().getID());
            updateShoppingCartItems(entry);
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateTotalPriceLabel(mainController.getShoppingCart());
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateVATLabel(mainController.getShoppingCart());
        });
        // Setting event handler for remove the product completely
        removeProductLabel.setOnMouseClicked(mouseEvent -> {

            removeProduct(entry, mainController);

            updateShoppingCartItems(entry);
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateTotalPriceLabel(mainController.getShoppingCart());
            mainController.getShoppingCartController().getShoppingCartFieldController().
                    updateVATLabel(mainController.getShoppingCart());
            mainController.getShoppingCartController().showShoppingCart();
        });

    }

    private void removeProduct(Map.Entry<Product, Integer>  entry,
                               MainController               mainController) {
        mainController.getShoppingCart().removeProductID(entry.getKey().getID());
        List<Node> nodes = mainController.getShoppingCartController().getShoppingCartFieldController().
                getShoppingCartProducts().getChildren();

        List<Node> nodeToRemove = new ArrayList<>();
        for (Node node : nodes){
            if(entry.getKey().getID().equals(node.getId())){
                nodeToRemove.add(node);
            }
        }
        mainController.getShoppingCartController().getShoppingCartFieldController().
                getShoppingCartProducts().getChildren().removeAll(nodeToRemove);

    }

}
