package SHOP.Shop;

import SHOP.Logic.Product;
import SHOP.Logic.ShoppingCart;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProductController {

    private static Map<String, Image> imageCache = new HashMap<>();

    @FXML
    private ImageView productImage;
    @FXML
    private Label productTitle, productPrice;
    @FXML
    private Label addToCartBtn;

    public void setProductBoxData(Product product) {
        MainController.getInstance().getScrollPane().setVvalue(0);
        Image pImage = imageCache.get(product.getID());
        if (pImage == null) {
            loadImageAsync(product);
        } else {
            productImage.setImage(pImage);
        }

        productImage.setId(product.getID());
        productImage.setOnMouseClicked(mouseEvent -> {
            int index = MainController.getInstance().getProducts().indexOf(product);
            MainController.getInstance().getProductPageController().showProduct(
                    MainController.getInstance().getProducts().get(index),
                    MainController.getInstance(),
                    MainController.getInstance().getScrollPane()
            );
        });

        productTitle.setText(product.getTitle());

        productPrice.setText(product.getPrice() + ",-");


        addToCartBtn.setId(product.getID());
        addToCartBtn.setOnMouseClicked(mouseEvent -> {
            ShoppingCart shoppingCart = MainController.getInstance().getShoppingCart();
            if (shoppingCart.getCart().isEmpty() ||
                    (product.getStock() > shoppingCart.getCart().getOrDefault(product, 0))) {
                ProductPageDataController.addProductToShoppingCartFromID(mouseEvent);
                animateAddedProduct();
            }
        });
    }

    private void loadImageAsync(Product product) {

        Task<Image> loadImageTask = new javafx.concurrent.Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(new File(product.getImageDirectory()).toURI().toString());
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image loadedImage = loadImageTask.getValue();
            imageCache.put(product.getID(), loadedImage);
            productImage.setImage(loadedImage);
        });

        Thread loadImageThread = new Thread(loadImageTask);
        loadImageThread.setDaemon(true);
        loadImageThread.start();
    }

    public void animateAddedProduct() {
        Bounds productImageBounds = productImage.localToScene(productImage.getBoundsInLocal());
        Bounds shoppingCartBounds = MainController.getInstance().getShoppingCartIcon().localToScene(
                MainController.getInstance().getShoppingCartIcon().getBoundsInLocal()
        );
        ImageView productImage = new ImageView(
                new Image(
                        new File(MainController.getInstance().getProducts()
                                .get(MainController.getInstance().getProducts().size() - 1).getImageDirectory())
                                .toURI().toString(), 50, 50, true, false)
        );
        ProductPageDataController.Animate(productImageBounds, shoppingCartBounds, productImage);
    }

    public void initialize() {
        // Set the add to cart button to a shopping cart icon
        addToCartBtn.setText("");
        addToCartBtn.setGraphic(new ImageView(
                new Image(
                        new File("src/main/resources/SHOP/Icons/shopping_cart_icon.png")
                                .toURI().toString(), 40, 40, true, false)
        ));
    }
}
