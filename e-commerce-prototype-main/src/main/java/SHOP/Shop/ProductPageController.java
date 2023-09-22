package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import SHOP.Logic.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ProductPageController {

    public void showProduct(Product product, MainController mainController, ScrollPane scrollPane){
        try {
            CMSHandler cmsHandler = new CMSHandler();
            FXMLLoader loader = cmsHandler.getPage("product_pages");
            VBox productPage = loader.load();
            ProductPageDataController productPageDataController = loader.getController();
            productPageDataController.updateProductPage(product,mainController);
            scrollPane.setContent(productPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
