package SHOP.Shop;

import SHOP.Logic.*;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    private static MainController main;
    private MainController() {
    }
    public static MainController getInstance(){
        if(main==null){
            main=new MainController();
        }
        return main;
    }
    private Scene scene;
    private CMSHandler cmsHandler = new CMSHandler();
    private ArrayList<Product> products = new ArrayList<>();
    @FXML
    private ImageView   shoppingCartIcon,
            loginIcon,
            loop;
    @FXML
    private TextField searchBar;
    @FXML
    private FlowPane productPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button  productHelperButton,
                    howToButton,
                    phone,
                    mouse,
                    cpu;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox leftBarBox;
    private ProductListController       productListController       = new ProductListController();
    private ProductPageController       productPageController       = new ProductPageController();
    private OrderController             orderController             = new OrderController();
    private ShoppingCartController      shoppingCartController      = new ShoppingCartController();
    private BlogPostsController         blogPostsController         = new BlogPostsController();
    private ShoppingCart                shoppingCart                = new ShoppingCart();
    private PHPageController            PHPageController            = new PHPageController();
    private CheckoutController          checkoutController          = new CheckoutController();
    private ProductHandler productHandler = new ProductHandler();
    private Search search = new Search();
    private Controller currentController;

    public void initialize(){
        products = productHandler.getProductList(true);
        //productListController.updateProductList(products);
        shoppingCartIcon.setOnMouseClicked(event -> {
            borderPane.setLeft(null);
            shoppingCartController.showShoppingCart();
            checkoutController.setOrder(null);
        });
        loginIcon.setOnMouseClicked(mouseEvent -> {
            borderPane.setLeft(null);
            LoginController loginController = (LoginController) setMainWindow("login_page");
            loginController.login();
        });
        howToButton.setOnMouseClicked(event -> {
            borderPane.setLeft(null);
            blogPostsController.showListView();
        });

        productHelperButton.setOnMouseClicked(event -> PHPageController.showProductHelper());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        loop.setOnMouseClicked(event -> {
            search.search();
        });
        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                   search.search();
                }
            }
        });
        phone.setOnMouseClicked(event -> {
            ArrayList<Product> list = search.getPhones();
            productListController.updateProductList(list);
        });
        mouse.setOnMouseClicked(event -> {
            ArrayList<Product> list = search.getMouse();
            productListController.updateProductList(list);
        });
        cpu.setOnMouseClicked(event -> {
            ArrayList<Product> list = search.getCPU();
            productListController.updateProductList(list);
        });

    }

    public Thread productLoadingThread(){
        Thread thread = new Thread(() -> {
            scene.setCursor(Cursor.WAIT);
            ProductHandler productHandler = new ProductHandler();
            long startTime = System.currentTimeMillis();
            products = productHandler.getProductList(true);
            long endTime = System.currentTimeMillis();
            System.out.println("Fetching took " + (endTime - startTime) + " milliseconds");
            Platform.runLater(() -> {

                long startTime1 = System.currentTimeMillis();
                productListController.updateProductList(products);
                long endTime1 = System.currentTimeMillis();
                System.out.println("Updating product-window took " + (endTime1 - startTime1) + " milliseconds");
            });
            scene.setCursor(Cursor.DEFAULT);
        });
        thread.setDaemon(true);
        return thread;
    }
    public void showHomePage(){
        borderPane.setLeft(leftBarBox);
        scrollPane.setContent(productPane);
        Thread thread = productLoadingThread();
        thread.start();
    }
    public Controller setMainWindow(String fileName){
        FXMLLoader fxmlLoader = cmsHandler.getPage(fileName);
        Controller controller;
        try{
            Parent parent = fxmlLoader.load();
            controller = fxmlLoader.getController();
            currentController = controller;
            scrollPane.setContent(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }
    // Getters
    public ArrayList<Product> getProducts() {
        return products;
    }
    public ShoppingCartController getShoppingCartController(){
        return shoppingCartController;
    }
    public CheckoutController getCheckoutController(){
        return checkoutController;
    }
    public ImageView getShoppingCartIcon(){
        return shoppingCartIcon;
    }
    public BorderPane getBorderPane(){
        return borderPane;
    }
    public ProductPageController getProductPageController(){ return productPageController;}
    public PHPageController getProductHelperPageController() {
        return PHPageController;
    }
    public ShoppingCart getShoppingCart(){
        return shoppingCart;
    }
    public ScrollPane getScrollPane(){
        return scrollPane;
    }
    public FlowPane getProductPane(){
        return productPane;
    }
    public Controller getCurrentController(){
        return currentController;
    }
    public void setScene(Scene scene){
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }
    public TextField getSearchBar(){
        return searchBar;
    }
    public VBox getLeftBarBox() {
        return leftBarBox;
    }
    public ProductListController getProductListController(){
        return productListController;
    }
}

