package CMS.Logic;

import CMS.Logic.component.*;
import CMS.Database.DBhandler;
import CMS.Database.DataBase;
import CMS.Logic.Exceptions.NotUniqueException;
import CMS.Logic.Exceptions.NotWithinBoundsException;
import CMS.Logic.component.*;
import CMS.Presentation.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class GUI_Controller implements Initializable, exportIntegration {
    private SelectableComponent selectableComponent;
    private ArrayList<component> componentsList;
    public ResultSet rsLoad;
    private final DataBase componentsDB = new DataBase(
            "db.kxzcqlftwnjqgfkpahvp.supabase.co",
            5432,
            "postgres",
            "postgres",
            "xpx2NfdK2Q865FKf"
    );

    private DBhandler compDBHandler = new DBhandler();

    private boolean isControlHeld = false;

    /* --- Stage --- */
    private Stage primaryStage;

    /* --- Menu --- */
    @FXML
    MenuItem newFileMenuButton, saveFileMenuButton, loadFileMenuButton, settingsMenuButton, exportMenuButton, closeFileMenuButton, exitMenuButton;

    /* COMPONENTS (left) */
    @FXML
    AnchorPane componentsMenu;
    @FXML
    private TextField components_searchbar;
    @FXML
    private GridPane components_containers, components_controls, components_menu, components_shapes;
    @FXML
    private Pane component_pane, component_roundpane, component_textblock, component_subscene, component_button, component_hyperlink, component_textfield, component_image;
    private GridPane currentComponentMenu;
    @FXML
    private ListView<component> components_listview;

    /* PREVIEW (middle) */
    @FXML
    private StackPane preview_background;
    @FXML
    private AnchorPane preview_window;

    /* PROPERTIES (right) */
    @FXML
    private AnchorPane propertiesMenu;
    @FXML
    private TextField properties_name, properties_fxid, properties_type, properties_width, properties_height, properties_x, properties_y, properties_textContent;
    @FXML
    private ColorPicker properties_color;
    @FXML
    private Button properties_create, properties_apply, properties_delete;
    @FXML
    private Label textOrLinkLabel, properties_error_label;
    @FXML
    private ChoiceBox<image> properties_imageChoiceBox;
    private Thread thread_properties_error_label;

    /* ------ */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage = GUI.getPrimaryStage();

        componentsList = new ArrayList<>();
        selectableComponent = new SelectableComponent(componentsList, components_listview, textOrLinkLabel, properties_imageChoiceBox, properties_name, properties_fxid, properties_type, properties_color, properties_textContent, properties_width, properties_height, properties_x, properties_y, properties_create, properties_apply, properties_delete);
        thread_properties_error_label = new Thread();

        /* Setting up menu items */
        saveFileMenuButton.setDisable(true);
        settingsMenuButton.setDisable(true);
        exportMenuButton.setDisable(true);
        closeFileMenuButton.setDisable(true);
        componentsMenu.setDisable(true);
        propertiesMenu.setDisable(true);

        /* Making components menu selectable */
        selectableComponent.makeSelectable(component_pane, true);
        selectableComponent.makeSelectable(component_image, true);
        selectableComponent.makeSelectable(component_roundpane, true);
        selectableComponent.makeSelectable(component_subscene, true);
        selectableComponent.makeSelectable(component_button, true);
        selectableComponent.makeSelectable(component_hyperlink, true);
        selectableComponent.makeSelectable(component_textfield, true);
        selectableComponent.makeSelectable(component_textblock, true);

        /* Fetch icon ResultSet from DAM and add them to the image ChoiceBox */
        ResultSet rs; // TODO: fetch rs from DAM
        properties_imageChoiceBox.getItems().add(new image("None", ""));
        properties_imageChoiceBox.getItems().add(new image("Test", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fenc1145monsters.fandom.com%2Fwiki%2FTemplate&psig=AOvVaw1W1CzRONt6S-Li9A0BW0-d&ust=1685390720509000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJC22evnmP8CFQAAAAAdAAAAABAJ"));
        /*
        if(rs != null) {
            try {
                //rs.first();
                if (!rs.isBeforeFirst()) {
                    System.out.println("No components in database.");
                    return;
                }
                while (rs.isBeforeFirst()) {
                    rs.next();
                }

                while (!rs.isAfterLast()) {
                    String name = rs.getString("name");
                    String path = rs.getString("path");
                    properties_imageChoiceBox.getItems().add(new image(name, path));
                    rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        */

        renderContent();
    }

    public void removeFocus() {
        preview_background.requestFocus();
    }
    public void keyPressHandler(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case DELETE:
                if(selectableComponent.getSelection() != null)  {
                    onDeleteComponentButtonClicked();
                    removeFocus();
                }
                break;
            case ESCAPE:
                if(selectableComponent.getSelection() != null)  {
                    deselectCurrentComponent();
                    removeFocus();
                }
                break;
            case CONTROL:
                isControlHeld = true;
                break;
            case COMMAND:
                isControlHeld = true;
                break;
            case S:
                if(isControlHeld) {
                    onSaveMenuButtonClicked();
                    removeFocus();
                }
                break;
            case N:
                if(isControlHeld) {
                    onNewFileMenuButtonClicked();
                    removeFocus();
                }
                break;
            case L:
                if(isControlHeld) {
                    onLoadMenuButtonClicked();
                    removeFocus();
                }
                break;
            case P:
                if(isControlHeld) {
                    onExportMenuButtonClicked();
                    removeFocus();
                }
                break;
        }
    }

    public void keyModifierHandler(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case CONTROL:
                isControlHeld = false;
                break;
            case COMMAND:
                isControlHeld = false;
                break;
        }
    }

    private void getComponentsFromDB(String dbTable, ArrayList<component> compList) {
        compList.clear();
        ResultSet rs = compDBHandler.getAllFromDB(componentsDB, dbTable);
        if(rs == null) { return; }
        try {
            //rs.first();
            if(!rs.isBeforeFirst())  {
                System.out.println("No components in database.");
                return;
            }
            while(rs.isBeforeFirst())  {
                rs.next();
            }

            while(!rs.isAfterLast()) {
                int typeID = rs.getInt("type_id");

                switch(typeID)  {
                    case 1:
                        subscene ss = new subscene(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color")));
                        getComponentsFromDB(ss.getFxid(), ss.getComponents_list());
                        compList.add(ss);
                        break;
                    case 2:
                        compList.add(new button(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color")),
                                rs.getString("components_text_content")));
                        break;
                    case 3:
                        compList.add(new pane(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color"))));
                        break;
                    case 4:
                        compList.add(new textblock(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color")),
                                rs.getString("components_text_content")));
                        break;
                    case 5:
                        compList.add(new roundpane(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color"))));
                        break;
                    case 6:
                        compList.add(new image(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color")),
                                rs.getString("components_text_content")));
                        break;
                    case 7:
                        compList.add(new textfield(rs.getString("components_name"),
                                rs.getString("fxid"),
                                rs.getDouble("components_x"),
                                rs.getDouble("components_y"),
                                rs.getDouble("components_width"),
                                rs.getDouble("components_height"),
                                Color.web(rs.getString("components_color")),
                                rs.getString("components_text_content")));
                }

                rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onContainersMenuButtonClicked(){
        toggleActiveComponentWindow(components_containers);
    }
    @FXML
    private void onControlsMenuButtonClicked(){
        toggleActiveComponentWindow(components_controls);
    }
    @FXML
    private void onMenuMenuButtonClicked(){
        toggleActiveComponentWindow(components_menu);
    }
    @FXML
    private void onShapesMenuButtonClicked(){
        toggleActiveComponentWindow(components_shapes);
    }
    private void toggleActiveComponentWindow(GridPane window){
        if(!(currentComponentMenu == null)){
            if(currentComponentMenu != window) {
                currentComponentMenu.setVisible(false);
                currentComponentMenu.setMinHeight(0);
                currentComponentMenu.setMaxHeight(0);
            }
        }
        if(window.isVisible()){
            window.setVisible(false);
            window.setMinHeight(0);
            window.setMaxHeight(0);
            currentComponentMenu = null;
        } else {
            window.setVisible(true);
            window.setMinHeight(200);
            window.setMaxHeight(200);
            currentComponentMenu = window;
        }
    }
    /* MENU BUTTON HANDLERS */
    private void toggleFileMenuButtons(boolean toggle) {
        saveFileMenuButton.setDisable(toggle);
        settingsMenuButton.setDisable(toggle);
        exportMenuButton.setDisable(toggle);
        closeFileMenuButton.setDisable(toggle);
        componentsMenu.setDisable(toggle);
        propertiesMenu.setDisable(toggle);
        if(toggle) { selectableComponent.deselectComponent(); }

    }
    @FXML
    private void onNewFileMenuButtonClicked() {
        saveConfirmationPrompt("Do you want to save your changes to: ", " before creating a new file?");
        try {
            FXMLLoader fxmlNewFileLoader = new FXMLLoader(GUI.class.getResource("NewFile.fxml"));
            Scene scene = new Scene(fxmlNewFileLoader.load(), 400, 400);
            NewFile_Controller controller = fxmlNewFileLoader.getController();
            Stage newFileStage = new Stage();
            newFileStage.setTitle("New File");
            newFileStage.setScene(scene);
            newFileStage.initModality(Modality.APPLICATION_MODAL);

            newFileStage.showAndWait();

            fileUpdater();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onSaveMenuButtonClicked()  {
        saveConfirmationPrompt("Overwrite and save changes to ", "?");
    }
    @FXML
    private void onLoadMenuButtonClicked(){
        saveConfirmationPrompt("Do you want to save your changes to: ", " before loading another file?");
        rsLoad = compDBHandler.getAllFromDB(componentsDB, "files");
        try {
            FXMLLoader fxmlSettingsLoader = new FXMLLoader(GUI.class.getResource("Load.fxml"));
            Scene scene = new Scene(fxmlSettingsLoader.load(), 400, 400);
            Stage loadStage = new Stage();
            loadStage.setTitle("Load file");
            loadStage.setScene(scene);
            loadStage.initModality(Modality.APPLICATION_MODAL);

            loadStage.showAndWait();

            fileUpdater();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Load file opened");
    }
    @FXML
    private void onSettingsMenuButtonClicked() {
        try {
            FXMLLoader fxmlSettingsLoader = new FXMLLoader(GUI.class.getResource("Settings.fxml"));
            Scene scene = new Scene(fxmlSettingsLoader.load(), 400, 400);
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.initModality(Modality.APPLICATION_MODAL);

            settingsStage.showAndWait();

            File.setIsSaved(false);
            settingsUpdater();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Settings opened");
    }
    @FXML
    private void onExportMenuButtonClicked()  {
        saveConfirmationPrompt("Do you want to save your changes to: ", " before exporting?");
        File.getCurrentFile().export();
    }
    @FXML
    private void onCloseFileMenuButtonClicked() {
        saveConfirmationPrompt("Do you want to save your changes to: ", " before closing?");
        File.setCurrentFile(null);
        componentsList.clear();
        fileUpdater();
    }
    @FXML
    private void onExitMenuButtonClicked() {
        saveConfirmationPrompt("Do you want to save: ", " before exiting?");
        System.exit(0);
    }
    public void saveConfirmationPrompt(String firstMsg, String secondMsg) {
        if(File.getCurrentFile() == null){
            return;
        }
        if(!File.getCurrentFile().isSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, firstMsg + "\"" + File.getCurrentFile().getFileName() + "\"" + secondMsg, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Save Confirmation");
            alert.setHeaderText("Save Confirmation");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                File.setIsSaved(true);
                compDBHandler.clearAndSaveComponents(componentsDB, componentsList, File.getCurrentFile().getFileName());
            }
        }
    }
    public void fileUpdater(){
        if(File.getCurrentFile() != null && File.isOpening()) {
            File.setIsSaved(true);
            File.setIsOpening(false);
            getComponentsFromDB(File.getCurrentFile().getFileName(), componentsList);
            File.getCurrentFile().setComponentsList(componentsList);

            toggleFileMenuButtons(false);
        }
        settingsUpdater();
        renderContent();
    }
    public void settingsUpdater(){
        if(File.getCurrentFile() == null) {
            toggleFileMenuButtons(true);
            preview_window.setMaxWidth(0);
            preview_window.setMaxHeight(0);
            primaryStage.setTitle("Content Management System");
            return;
        }
        preview_window.setMaxWidth(File.getCurrentFile().getViewport_width());
        preview_window.setMaxHeight(File.getCurrentFile().getViewport_height());
        primaryStage.setTitle("Content Management System - " + File.getCurrentFile().getFileName());
    }
    /* -------------------------------- PROPERTIES / CREATE COMPONENT ---------------------------------------------- */
    @FXML
    private void onCreateComponentButtonClicked(){
        try {
            String name = String.valueOf(properties_name.getText());
            String fxid = String.valueOf(properties_fxid.getText());
            String type = String.valueOf(properties_type.getText());
            Double width = Double.parseDouble(properties_width.getText());
            Double height = Double.parseDouble(properties_height.getText());
            Double x = Double.parseDouble(properties_x.getText());
            Double y = Double.parseDouble(properties_y.getText());
            Color color = properties_color.getValue();
            String textContent = "";
            String imagePath = "";
            if(selectableComponent.getSelection() instanceof image) {
                imagePath = ((image) (properties_imageChoiceBox.getSelectionModel().getSelectedItem())).getPath();
            } else {
                textContent = properties_textContent.getText();
            }


            // Checks
            if(!isUniqueComponentName(name)){ throw new NotUniqueException("Name must be unique"); }
            contentInXYBoundsChecker(x, y);
            width = contentWithinWidthChecker(width, x);
            height = contentWithinHeightChecker(height, y);

            switch(type){
                case "PANE":
                    componentsList.add(new pane(name, fxid, x, y, width, height, color));
                    break;
                case "ROUNDPANE":
                    componentsList.add(new roundpane(name, fxid, x, y, width, height, color));
                    break;
                case "TEXTBLOCK":
                    componentsList.add(new textblock(name, fxid, x, y, width, height, color, textContent));
                    break;
                case "SUBSCENE":
                    componentsList.add(new subscene(name, fxid, x, y, width, height, color));
                    break;
                case "BUTTON":
                    componentsList.add(new button(name, fxid, x, y, width, height, color, textContent));
                    break;
                case "IMAGE":
                    componentsList.add(new image(name, fxid, x, y, width, height, color, imagePath));
                    break;
                case "TEXTFIELD":
                    componentsList.add(new textfield(name, fxid, x, y, width, height, color, textContent));
                    break;
            }
            File.setIsSaved(false);
            sendPropertiesErrorMessage("Component created!", false);
            renderContent();

        } catch (NumberFormatException ex){
            System.out.println("NumberFormatException!");
            sendPropertiesErrorMessage("Number format exception!", true);
        } catch (NotUniqueException ex) {
            properties_name.requestFocus();
            sendPropertiesErrorMessage(ex.getMessage(), true);
        } catch (NotWithinBoundsException ex) {
            sendPropertiesErrorMessage(ex.getMessage(), true);
        }
    }
    public void onApplyComponentButtonClicked() {
        component selection = selectableComponent.getSelection();
        try {
            String newName = String.valueOf(properties_name.getText());
            String newFxid = String.valueOf(properties_fxid.getText());
            Double newWidth = Double.parseDouble(properties_width.getText());
            Double newHeight = Double.parseDouble(properties_height.getText());
            Double newX = Double.parseDouble(properties_x.getText());
            Double newY = Double.parseDouble(properties_y.getText());
            Color newColor = properties_color.getValue();
            String newTextContent = properties_textContent.getText();

            // If component name changes, only apply the new name if it's unique
            if(!selection.getName().equals(newName) && !isUniqueComponentName(newName)) { throw new NotUniqueException("Name must be unique"); }

            // Check if component contains display text
            if(selection instanceof ContainsText) {((ContainsText) selection).editText(newTextContent);}
            if(selection instanceof ContainsLink) {((ContainsLink) selection).editLink(newTextContent);}

            if(contentInXYBoundsChecker(newX, newY)) {
                selection.setPositionX(newX);
                selection.setPositionY(newY);
            }
            selection.setName(newName);
            selection.setFxid(newFxid);
            selection.setSizeW(contentWithinWidthChecker(newWidth, newX));
            selection.setSizeH(contentWithinHeightChecker(newHeight, newY));
            selection.setColor(newColor);

            File.setIsSaved(false);
            sendPropertiesErrorMessage("Component updated!", false);
            deselectCurrentComponent();
            renderContent();
        } catch (NumberFormatException ex){
            System.out.println("NumberFormatException!");
            sendPropertiesErrorMessage("Number format exception!", true);
        } catch (NotUniqueException ex) {
            properties_name.requestFocus();
            properties_name.setText(selection.getName());
            sendPropertiesErrorMessage(ex.getMessage(), true);
        } catch (NotWithinBoundsException ex) {
            sendPropertiesErrorMessage(ex.getMessage(), true);
        }
    }
    private boolean contentInXYBoundsChecker(Double x, Double y) throws NotWithinBoundsException {
        if((x < 0) || (x > preview_window.getWidth())) {    // Check if X coord is within bound
            throw new NotWithinBoundsException("Invalid x value");
        } else if((y < 0) || (y > preview_window.getHeight())) {   // Check if Y coord is within bound
            throw new NotWithinBoundsException("Invalid y value");
        } else {
            return true;
        }
    }
    private Double contentWithinWidthChecker(Double width, Double x) {
        // Check if width is within bound
        if((width + x) <= preview_window.getWidth() && (x >= 0)){
            return width;
        } else {
            return (preview_window.getWidth() - x);
        }
    }
    private Double contentWithinHeightChecker(Double height, Double y) {
        // Check if height is within bound
        if((height + y) <= preview_window.getHeight() && y >= 0){
            return height;
        } else {
            return (preview_window.getHeight() - y);
        }
    }
    public void onDeleteComponentButtonClicked() {
        componentsList.remove(selectableComponent.getSelection());
        sendPropertiesErrorMessage("Component deleted!", false);
        deselectCurrentComponent();
        renderContent();
    }
    private boolean isUniqueComponentName(String name){
        for(component comp : componentsList){
            if(comp.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
    public void deselectCurrentComponent() {
        selectableComponent.deselectComponent();
        selectableComponent.resetProperties();
    }
    public void sendPropertiesErrorMessage(String message, boolean error) {
        if(thread_properties_error_label.isAlive()){
            thread_properties_error_label.interrupt();
        }
        properties_error_label.setText(message);
        properties_error_label.setVisible(true);
        thread_properties_error_label = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!error){
                    properties_error_label.setStyle("-fx-text-fill: green");
                } else {
                    properties_error_label.setStyle("-fx-text-fill: red");
                }
                try {
                    Thread.sleep(7000);
                    properties_error_label.setVisible(false);
                } catch (InterruptedException ex) {}
            }
        });
        thread_properties_error_label.setDaemon(true);
        thread_properties_error_label.start();
    }
    public void renderContent()    {
        preview_window.getChildren().clear();
        components_listview.getItems().clear();

        for (component comp : componentsList)    {
            if(comp instanceof subscene) {
                getComponentsFromDB(comp.getFxid(), ((subscene) comp).getComponents_list());
            }
            preview_window.getChildren().add(comp.compConvert());
            setComponents_listview(comp);
        }
        componentsDraggableUpdater();   // Updates the draggable property for all components after each render
    }
    private void componentsDraggableUpdater(){
        for (Node node : preview_window.getChildren()){
            selectableComponent.makeSelectable(node, false);
        }
    }
    public void setComponents_listview(component comp) {
        components_listview.getItems().add(comp);
    }
    @FXML
    private void components_listviewHandler() {
        selectableComponent.updateComponentSelection(components_listview.getSelectionModel().getSelectedItem().getReference());
    }

    @Override
    public String getFilePath(String identifier) {
        ResultSet rs = compDBHandler.getAllFromDB(componentsDB, "files");

        try {
            while(rs.next()) {
                if(rs.getString("filepath").equals(identifier)) {
                    return rs.getString("exportpath");
                }
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public ResultSet getExportData() {
        return compDBHandler.getAllFromDB(componentsDB, "files");
    }
}