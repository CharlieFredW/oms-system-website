package PIM.presentation;

import PIM.domain.PersistenceHandler;
import PIM.domain.Product;
import PIM.domain.ProductTagPairing;
import PIM.domain.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.*;


public class PIMController implements Initializable {
    @FXML
    public TextArea Name, Description, Price, Quantity, Search,editNameField, editDescriptionField, editQuantityField, editPriceField;
    @FXML
    public Label productIdLabel;
    @FXML
    public Button Add, Clear, updateProductButton, goToEditPageButton,removeProductButton;
    @FXML
    public ToggleButton idToggleButton, nameToggleButton, tagToggleButton;
    @FXML
    public ToggleGroup searchByToggle;
    @FXML
    public ListView<HBox> list_Of_Products;
    @FXML
    public TabPane pimTabPane;

    @FXML
    //Standard "select one" checkbox
    public ComboBox<String> Categories, Colors, editCategoriesBox, editColorsBox;  //ControlsFX (library for javaFX) "select multiple" checkbox
    public CheckComboBox<String> Tags, editTagsBox;
    private HashMap<String, Boolean> activeTags = new HashMap<>();
    private Product product;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("This method is reached: Initialize");
        initiateTextFields();
        initiateCategories();
        initiateTags();
        initiateColors();
        tagsListener();
        addListener();
        checkIfFilled();
        clearFields();
        //dummyProducts();
        searchbarListener();
        initiateEditCategories();
        initiateEditColors();
        initiateEditTags();

    }
    /**
     Sets up the behavior for initializing text fields for the given 'from' and 'to' controls.
     Essentially, when the user presses the TAB key while the 'from' control is focused, the focus is transferred to the 'to' control.
     In addition, any tabs in the 'from' TextArea are removed.
     Note that the reason why we need to pass a Control object to a TextArea object is because Categories is a ComboBox,
     which cannot be passed as an argument to setInitiateTextFields if it only accepts TextAreas as arguments.
     @param from The control that initiates the text field behavior.
     @param to The control to transfer focus to.
     */
    private void setInitiateTextFields(Control from, Control to) {
        from.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                to.requestFocus();
                if (from instanceof TextArea) {
                    ((TextArea)from).setText(((TextArea)from).getText().replaceAll("\t", ""));
                }
                event.consume();
            }
        });
    }
    /*
    Method sets up the text field behavior for the four fields of
    the product form. When a field is focused and the user presses the TAB key,
    focus is transferred to the next field. For instance, switches focus from 'name' to 'description'.
     */
    private void initiateTextFields() {
        setInitiateTextFields(Name, Description);
        setInitiateTextFields(Description, Price);
        setInitiateTextFields(Price, Quantity);
        setInitiateTextFields(Quantity, Categories);
    }

    /**
     * A lot of duplicate code in the initiateCategories() and initiateEditCategories() methods, and the
     * same for the initiateColors() and initiateEditColors() methods.
     */
    private void initiateCategories() {
        ObservableList<String> categoriesList = FXCollections.observableArrayList(
                "Laptop"
                , "Desktop PC"
                , "Mouse"
                , "Monitor"
                , "Keyboard"
                , "Disk Drive"
                , "Motherboard"
                , "CPU"
                , "RAM"
                , "GPU"
                , "Phone"
                , "Camera"
        );
        Categories.setItems(categoriesList);
        Categories.setPromptText("Categories");
        Categories.setButtonCell(createListCell("Categories"));
    }

    private void initiateEditCategories() {
        ObservableList<String> categoriesList = FXCollections.observableArrayList(
                "Laptop"
                , "Desktop PC"
                , "Mouse"
                , "Monitor"
                , "Keyboard"
                , "Disk Drive"
                , "Motherboard"
                , "CPU"
                , "RAM"
                , "GPU"
                , "Phone"
                , "Camera"
        );
        editCategoriesBox.setItems(categoriesList);
        editCategoriesBox.setPromptText("Categories");
        editCategoriesBox.setButtonCell(createListCell("Categories"));
    }

    private void initiateColors() {
        ObservableList<String> color_List = FXCollections.observableArrayList(
                "Red"
                , "Yellow"
                , "Blue"
                , "Green"
                , "Purple"
                , "Pink"
                , "Black"
                , "Grey"
                , "White"
                , "Brown"
        );
        Colors.setItems(color_List);
        Colors.setPromptText("Color");
        Colors.setButtonCell(createListCell("Color"));
    }

    private void initiateEditColors() {
        ObservableList<String> color_List = FXCollections.observableArrayList(
                "Red"
                , "Yellow"
                , "Blue"
                , "Green"
                , "Purple"
                , "Pink"
                , "Black"
                , "Grey"
                , "White"
                , "Brown"
        );
        editColorsBox.setItems(color_List);
        editColorsBox.setPromptText("Color");
        editColorsBox.setButtonCell(createListCell("Color"));
    }

    /**
     Returns a custom ListCell with the specified prompt text. The ListCell displays the item, or if the item
     is empty, displays the prompt text instead.
     @param promptText The prompt text to display when the ListCell is empty.
     @return A custom ListCell object.
     */
    private ListCell<String> createListCell(String promptText) {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? promptText : item);
            }
        };
    }
    /*
    private void initiateColors() {
        ObservableList<String> color_List = FXCollections.observableArrayList("Red", "Yellow", "Blue", "Green", "Purple", "Pink");
        Colors.setItems(color_List);
        //Max amount of visible rows
        Colors.setVisibleRowCount(5);
    }
    */
    private void initiateTags() {
        // Get all tags from the DB
        List<Tag> tagList = PersistenceHandler.getInstance().findAllTags();

        // Add names of the found tags to ArrayList in lower case
        ArrayList<String> tagNameList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagNameList.add(tag.getName().toLowerCase());
        }
        ObservableList<String> tags_List = FXCollections.observableArrayList(tagNameList);
        Tags.getItems().addAll(tags_List);
        Tags.setTitle("Tags");
    }

    private void initiateEditTags() {
        // Get all tags from the DB
        List<Tag> tagList = PersistenceHandler.getInstance().findAllTags();

        // Add names of the found tags to ArrayList in lower case
        ArrayList<String> tagNameList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagNameList.add(tag.getName().toLowerCase());
        }
        ObservableList<String> tags_List = FXCollections.observableArrayList(tagNameList);
        editTagsBox.getItems().addAll(tags_List);
        editTagsBox.setTitle("Tags");
    }

    private void searchbarListener() {
        Search.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    showSearchProducts();
                    Search.setText("");
                    ke.consume();
                }
            }
        });
    }
    private void tagsListener() {
        /*
        Takes the Tags checkcombobox object, and adds an eventlistener on the getCheckedItems property.
         */
        Tags.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            /*
            This whole eventlistener runs twice on event fired. It is a known issue by the developer of the library,
            and has yet to be fixed as of coding this.
             */
            @Override
            public void onChanged(Change<? extends String> change) {
                /*
                change.next returns if there are multiple changes since last change as a boolean.
                Since we check everytime a change is made, it is redudant, but must be called for the listener to work
                */
                while (change.next()) {
                    activeTags.clear();
                    System.out.println(change);
                    System.out.println(Tags.getCheckModel().getCheckedItems());
                    Tags.getCheckModel().getCheckedItems().forEach((e) -> {
                        activeTags.put(e, true);
                    });
                }
                System.out.println(activeTags);
            }
        });
    }


    /*
    private void dummyProducts() {
        //Dummy test array. Items to be altered in future use
        ObservableList<String> dummy = FXCollections.observableArrayList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
        list_Of_Products.setItems(dummy);


    }
     */

    /*
     This method clears all the input fields and selections in the UI, preparing it for a new input.
     */
    private void clear() {
        Name.setText("");
        Description.setText("");
        Quantity.setText("");
        Price.setText("");
        editNameField.setText("");
        editDescriptionField.setText("");
        editPriceField.setText("");
        editQuantityField.setText("");
        Categories.getSelectionModel().clearSelection();
        Colors.getSelectionModel().clearSelection();
        Tags.getCheckModel().clearChecks();
        editColorsBox.getSelectionModel().clearSelection();
        editCategoriesBox.getSelectionModel().clearSelection();
    }

    public void clearList() {
        list_Of_Products.getItems().clear();
    }
    public void clearFields() {
        Clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clear();
            }
        });
    }

    private void showSearchProducts() {
        ObservableList<HBox> productSearch = FXCollections.observableArrayList();
        Label headerNameLabel = new Label("Name");
        headerNameLabel.setPrefWidth(45);
        headerNameLabel.setWrapText(true);
        Label headerPriceLabel = new Label("Price");
        headerPriceLabel.setPrefWidth(45);
        headerPriceLabel.setWrapText(true);
        Label headerQuantityLabel = new Label("Qty");
        headerQuantityLabel.setPrefWidth(45);
        headerQuantityLabel.setWrapText(true);
        Label headerColorLabel = new Label("Color");
        headerColorLabel.setPrefWidth(45);
        headerColorLabel.setWrapText(true);
        HBox headerBox = new HBox(headerNameLabel, headerPriceLabel, headerQuantityLabel, headerColorLabel);
        headerBox.setSpacing(45);
        headerBox.setStyle("-fx-font-weight: bold");
        productSearch.add(headerBox);

        if (searchByToggle.getSelectedToggle() == idToggleButton) {
            System.out.println("searching for products by id.");
            Product fetchedProduct = PersistenceHandler.getInstance().findProductByID(Search.getText().trim());
            try {
                HBox productBox = createProductHBox(fetchedProduct);
                productSearch.add(productBox);
            } catch (NullPointerException npe) {
                System.out.println("NullPointerException caught - no products found with that id.");
            }

        } else if (searchByToggle.getSelectedToggle() == tagToggleButton) {
            System.out.println("searching for products by tag.");
            try {
                List<Product> fetchedProducts = PersistenceHandler.getInstance().searchProductsByTag(Search.getText());
                fetchedProducts.forEach((product) -> {
                    HBox hbox = createProductHBox(product);
                    productSearch.add(hbox);
                });
            } catch(NullPointerException npe) {
                System.out.println("No tag was found. Did you spell it right?");
            }
        } else {
            System.out.println("searching for products by name.");
            List<Product> fetchedProducts = PersistenceHandler.getInstance().searchProductsByName(Search.getText(), 3);
            fetchedProducts.forEach((product) -> {
                HBox hbox = createProductHBox(product);
                productSearch.add(hbox);
            });
        }
        list_Of_Products.setItems(productSearch);
    }

    private HBox createProductHBox(Product product) {
        Integer minMaxWidth = 45;
        HBox hbox = new HBox();
        Label nameLabel = new Label(product.getName());
        nameLabel.setPrefWidth(minMaxWidth);
        nameLabel.setWrapText(true);
        Label priceLabel = new Label(Double.toString(product.getPrice()));
        priceLabel.setPrefWidth(minMaxWidth);
        priceLabel.setWrapText(true);
        Label quantityLabel = new Label(Integer.toString(product.getStock()));
        quantityLabel.setPrefWidth(minMaxWidth);
        quantityLabel.setWrapText(true);
        Label colorLabel = new Label(product.getColor());
        colorLabel.setPrefWidth(minMaxWidth);
        colorLabel.setWrapText(true);
        hbox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, colorLabel);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(minMaxWidth);
        return hbox;
    }


    @FXML
    public void addListener() {
        Add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
                Product build_Product = productConstructor();
                persistenceHandler.addProduct(build_Product);
                // Creating list over all checked tags in the combobox
                ObservableList<String> productTags = Tags.getCheckModel().getCheckedItems();
                // Creating a ProductTagPairing for each tag and storing it in the DB
                for (String tagName : productTags) {
                    persistenceHandler.addProductTagPairing(productTagPairingConstructor(
                            build_Product.getProduct_id(),
                            persistenceHandler.findTagByName(tagName).getTag_id()));
                }
                clear();
            }
        });
    }

    public void updateProductButtonHandler(){
        Product existingProduct = PersistenceHandler.getInstance().findProductByID(productIdLabel.getText().trim());
        existingProduct.setName(editNameField.getText().trim());
        existingProduct.setDescription(editDescriptionField.getText().trim());
        existingProduct.setPrice(Double.parseDouble(editPriceField.getText().trim()));
        existingProduct.setStock(Integer.parseInt(editQuantityField.getText().trim()));
        existingProduct.setColor(editColorsBox.getValue());
        existingProduct.setCategory(editCategoriesBox.getValue());
        PersistenceHandler.getInstance().updateProduct(existingProduct);
        pimTabPane.getSelectionModel().select(0);
        clear();
    }


    private void checkIfFilled() {
        Add.setDisable(true);
        Name.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Description.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Price.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Categories.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Colors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateAddButton();
        });
        Tags.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super String>) (event) -> {
            updateAddButton();
        });
    }

    private void updateAddButton() {
        if (!Tags.getCheckModel().getCheckedItems().isEmpty()
                && !Colors.getSelectionModel().isEmpty()
                && !Categories.getSelectionModel().isEmpty()
                && !Name.getText().isEmpty()
                && !Description.getText().isEmpty()
                && !Quantity.getText().isEmpty()
                && !Price.getText().isEmpty()) {
            Add.setDisable(false);
        } else {
            Add.setDisable(true);
        }
    }

    private Product productConstructor() {
        return new Product(
                Name.getText(),
                Description.getText(),
                Colors.getValue(),
                Integer.parseInt(Quantity.getText()),
                Categories.getValue(),
                Double.parseDouble(Price.getText()),
                1.0
        );
    }
    private ProductTagPairing productTagPairingConstructor(String productId, String tagId) {
        return new ProductTagPairing(productId, tagId);
    }

    @FXML
    public void removeProductHandler() {
        Node node = list_Of_Products.getSelectionModel().getSelectedItem().getChildren().get(0);
        Label label = (Label) node;
        String name = label.getText();
        ArrayList <Product> tempProducts = new ArrayList<>();
        tempProducts.addAll(PersistenceHandler.getInstance().findAllProducts());

        for(Product p : tempProducts){
            if(name.trim().equals(p.getName())){
                PersistenceHandler.getInstance().deleteProduct(p);
            }
        }
        clearList();
    }


    @FXML
    public void retrieveInfoAndSendToEditPage() {
        Node node = list_Of_Products.getSelectionModel().getSelectedItem().getChildren().get(0);
        Label label = (Label) node;
        String name = label.getText();

        ArrayList <Product> tempProducts = new ArrayList<>();
        tempProducts.addAll(PersistenceHandler.getInstance().findAllProducts());

        for(Product p : tempProducts){
            if(name.trim().equals(p.getName())){

                editNameField.setText(p.getName());
                editDescriptionField.setText(p.getDescription());
                editPriceField.setText(String.valueOf(p.getPrice()));
                editQuantityField.setText(String.valueOf(p.getStock()));
                editCategoriesBox.setValue(p.getCategory());
                editColorsBox.setValue(p.getColor());
                productIdLabel.setText(p.getProduct_id());
            }

            pimTabPane.getSelectionModel().select(2);
            Search.setText("");
            list_Of_Products.getItems().clear();

        }
    }

    @FXML
    public void goFromEditToSearchTab(){
        pimTabPane.getSelectionModel().select(1);

    }



}