package SHOP.Shop;

import SHOP.Logic.Product;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.File;

public class ProductPageDataController {

    @FXML
    private Label productPrice, productDescription, productTitle, addToCartBtn;
    @FXML
    private ImageView productImageView;

    public void updateProductPage(Product           product,
                                  MainController    mainController){
        String price = product.getPrice() + ",-";
        productPrice.setText(price);
        productTitle.setText(product.getTitle());
        productDescription.setText(product.getDescription());
        Image pImage =  new Image(
                new File(product.getImageDirectory()).toURI().toString());
        productImageView.setImage(pImage);

        addToCartBtn.setText("");
        addToCartBtn.setGraphic(new ImageView(
                new Image(
                        new File("src/main/resources/SHOP/Icons/shopping_cart_icon.png").
                                toURI().toString(),40,40,true, false)));
        addToCartBtn.setId(product.getID());
        addToCartBtn.setOnMouseClicked(mouseEvent -> {
            animateAddedProduct();
            addProductToShoppingCartFromID(mouseEvent);
        });

    }

    static void addProductToShoppingCartFromID(MouseEvent       mouseEvent) {
        String id = ((Label) mouseEvent.getSource()).getId();
        int index = 0;
        for(Product product1 : MainController.getInstance().getProducts()){
            if(product1.getID().equals(id)){
                index = MainController.getInstance().getProducts().indexOf(product1);
            }
        }
        MainController.getInstance().getShoppingCart().cartAdd(MainController.getInstance().getProducts().get(index),1);

    }

    public void animateAddedProduct(){
        Bounds productImageBounds = this.productImageView.localToScene(this.productImageView.getLayoutBounds());
        Bounds shoppingCartBounds = MainController.getInstance().getShoppingCartIcon().localToScene(MainController.getInstance().getShoppingCartIcon().getBoundsInLocal());
        ImageView productImage = new ImageView(productImageView.getImage());
        productImage.setFitHeight(50);
        productImage.setFitWidth(50);
        Animate(productImageBounds, shoppingCartBounds, productImage);
    }

    public static void Animate(Bounds productImageBounds, Bounds shoppingCartBounds, ImageView productImage) {
        MainController.getInstance().getBorderPane().getChildren().add(productImage);
        Path path = new Path();
        path.getElements().add(
                new MoveTo(
                        productImageBounds.getCenterX(),
                        productImageBounds.getCenterY()));
        path.getElements().add(
                new ArcTo(
                        50,
                        50,
                        0,
                        shoppingCartBounds.getCenterX(),
                        shoppingCartBounds.getCenterY(),
                        false,
                        false));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2000));
        pathTransition.setNode(productImage);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.play();
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(2000));
        scaleTransition.setNode(productImage);
        scaleTransition.setByX(0.5);
        scaleTransition.setByY(0.5);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);
        scaleTransition.play();
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(2500));
        fadeTransition.setNode(productImage);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> MainController.getInstance().getBorderPane().getChildren().remove(productImage));
    }
}
