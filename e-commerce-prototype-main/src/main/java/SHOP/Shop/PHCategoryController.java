package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import SHOP.Logic.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class PHCategoryController implements Controller {
    // Category
    @FXML
    private FlowPane contentDisplay;
    private CMSHandler cmsHandler = new CMSHandler();
    // Price
    @FXML
    private Label textLabel;
    @FXML
    Button homePageButton;

    public ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        for(Product product : MainController.getInstance().getProducts()){
            if(!categories.contains(product.getCategory())){
                categories.add(product.getCategory());
            }
        }
        return categories;
    }

    public void fillCategories() {
        try {
            for (String string : getCategories()) {
                FXMLLoader fxmlLoader = cmsHandler.getPage("PHCategory");
                VBox category = fxmlLoader.load();
                PHCategoryDataController phCategoryDataController = fxmlLoader.getController();
                phCategoryDataController.loadCategories(string);
                contentDisplay.getChildren().add(category);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resizeContentDisplay();
    }

    public void displayProducts(){
        textLabel.setText("The products we would recommend are");
        resizeContentDisplay();
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for(Product product : MainController.getInstance().getProducts()){
            if(product.getCategory().equals(PHCategoryDataController.getCategory())){
                if(product.getPrice() >= PHCategoryDataController.getMinPrice() &&
                        product.getPrice() <= PHCategoryDataController.getMaxPrice()) {
                    filteredProducts.add(product);
                }
            }
        }
        filteredProducts.sort(Comparator.comparingDouble(Product::getPrice));

        if(filteredProducts.isEmpty()){
            textLabel.setText("There are no products matching your preferences");
            contentDisplay.getChildren().add(homePageButton);
            homePageButton.setOnMouseClicked(mouseEvent -> {
                MainController.getInstance().showHomePage();
            });
        } else {
            if(filteredProducts.size() >= 1) {
                loadProduct(filteredProducts.get(0));
            }
            if (filteredProducts.size() >= 2) {
                loadProduct(filteredProducts.get((filteredProducts.size())/2));
            }
            if (filteredProducts.size() >= 3) {
                loadProduct(filteredProducts.get(filteredProducts.size()-1));
            }
        }
    }

    public void loadProduct(Product product){
        try{
            CMSHandler cmsHandler = new CMSHandler();
            FXMLLoader fxmlLoader = cmsHandler.getPage("gen_product");
            VBox productBox = fxmlLoader.load();
            ProductController productController = fxmlLoader.getController();
            productController.setProductBoxData(product);
            contentDisplay.getChildren().add(productBox);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void resizeContentDisplay(){
        contentDisplay.setPrefSize(MainController.getInstance().getScrollPane().getWidth(),MainController.getInstance().getScrollPane().getHeight());
    }

    public void initialize() {
        contentDisplay.getChildren().remove(homePageButton);
    }
}
