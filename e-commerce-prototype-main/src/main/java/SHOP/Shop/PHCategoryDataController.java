package SHOP.Shop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class PHCategoryDataController implements Controller {
    @FXML
    private VBox priceRangeBox;
    @FXML
    private Button categoryButton, useCaseButton;
    @FXML
    private ImageView categoryImage;
    // price range
    @FXML
    private Button acceptPriceRangeButton;
    @FXML
    private TextField minRangeTextField, maxRangeTextField;
    private static String category;
    private static double minPrice, maxPrice;

    public void loadCategories(String string) {
        categoryButton.setText(string);
        categoryButton.setId(string);
        categoryImage.setImage(new Image(new File("src/main/resources/SHOP/Icons/product_dummy.jpg").toURI().toString()));

        categoryButton.setOnMouseClicked(mouseEvent -> {
            MainController.getInstance().setMainWindow("PHUseCase");
            category = ((Button)mouseEvent.getSource()).getId();
            System.out.println("Chosen category: " + category);
        });
    }

    @FXML
    public void loadPriceRange() {
        MainController.getInstance().setMainWindow("PHPriceRange");
        ((PHCategoryDataController)MainController.getInstance().getCurrentController()).resizePriceRangeBox();
        ((PHCategoryDataController)MainController.getInstance().getCurrentController()).setAcceptPriceRangeButton();
        ((PHCategoryDataController)MainController.getInstance().getCurrentController()).restrainTextFields();
    }

    public void setAcceptPriceRangeButton(){
        acceptPriceRangeButton.setOnMouseClicked(mouseEvent -> {
            if (!minRangeTextField.getText().isEmpty() && !maxRangeTextField.getText().isEmpty()) {
                MainController.getInstance().setMainWindow("PHCategorySelection");
                minPrice = Double.parseDouble(minRangeTextField.getText());
                maxPrice = Double.parseDouble(maxRangeTextField.getText());
                ((PHCategoryController) MainController.getInstance().getCurrentController()).displayProducts();
            }
        });
    }

    public void restrainTextFields() {
        minRangeTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                minRangeTextField.setText(t1.replaceAll("\\D", ""));
            }
        });

        maxRangeTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                maxRangeTextField.setText(t1.replaceAll("\\D", ""));
            }
        });
    }

    public void resizePriceRangeBox(){
        priceRangeBox.setPrefSize(MainController.getInstance().getScrollPane().getWidth(),MainController.getInstance().getScrollPane().getHeight());
    }

    public static double getMinPrice(){
        return minPrice;
    }
    public static double getMaxPrice(){
        return maxPrice;
    }
    public static String getCategory(){
        return category;
    }
}