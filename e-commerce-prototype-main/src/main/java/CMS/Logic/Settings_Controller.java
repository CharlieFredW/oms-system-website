package CMS.Logic;

import CMS.Database.DBhandler;
import CMS.Database.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings_Controller implements Initializable {
    Stage stage;
    /* DB */
    private final DataBase componentsDB = new DataBase(
            "db.kxzcqlftwnjqgfkpahvp.supabase.co",
            5432,
            "postgres",
            "postgres",
            "xpx2NfdK2Q865FKf"
    );

    private DBhandler dBhandler = new DBhandler();
    private ErrorMessageHandler errorMessageHandler;
    @FXML
    Label errorMessageLabel;
    @FXML
    TextField fileNameTextfield, viewportWidthTextfield, viewportHeightTextfield;
    @FXML
    ChoiceBox<String> containerChoiceBox, wrapperChoiceBox;
    @FXML
    Button saveButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessageHandler = new ErrorMessageHandler(errorMessageLabel, 5, "red");
        containerChoiceBox.getItems().addAll("AnchorPane", "BorderPane", "FlowPane", "StackPane", "ScrollPane");
        wrapperChoiceBox.getItems().addAll("None", "HBox", "VBox", "HBox+VBox", "VBox+HBox");

        fileNameTextfield.setText(File.getCurrentFile().getFileName());
        containerChoiceBox.getSelectionModel().select(File.getCurrentFile().getContainerType());
        wrapperChoiceBox.getSelectionModel().select(File.getCurrentFile().getWrapperType());
        viewportWidthTextfield.setText(String.valueOf(File.getCurrentFile().getViewport_width()));
        viewportHeightTextfield.setText(String.valueOf(File.getCurrentFile().getViewport_height()));
    }

    public void onSaveButtonClicked() {
        if(fileNameTextfield.getText().isEmpty()){
            errorMessageHandler.displayError("Please fill in a name.");
            fileNameTextfield.requestFocus();
            System.out.println("Please fill in a name.");
            return;
        }
        if(containerChoiceBox.getSelectionModel().isEmpty()) {
            errorMessageHandler.displayError("Please select a container.");
            containerChoiceBox.requestFocus();
            System.out.println("Please select a container.");
            return;
        }
        if(viewportWidthTextfield.getText().isEmpty() || !viewportWidthTextfield.getText().matches("[0-9]+")) {
            errorMessageHandler.displayError("Please fill in a valid viewport width.");
            viewportWidthTextfield.requestFocus();
            System.out.println("Please fill in a viewport width.");
            return;
        }
        if(viewportHeightTextfield.getText().isEmpty() || !viewportHeightTextfield.getText().matches("[0-9]+")) {
            errorMessageHandler.displayError("Please fill in a valid viewport height.");
            viewportHeightTextfield.requestFocus();
            System.out.println("Please fill in a viewport height.");
            return;
        }
        if(fileNameTextfield.getText().contains(" ")){
            errorMessageHandler.displayError("File name cannot contain spaces.");
            fileNameTextfield.requestFocus();
            System.out.println("Please fill in a file name without spaces.");
            return;
        }
        if(!(File.getCurrentFile().getFileName().equals(fileNameTextfield.getText()))){
            if(!File.isUniqueFileName(fileNameTextfield.getText(), componentsDB)){
                errorMessageHandler.displayError("A file with this name already exists.");
                fileNameTextfield.requestFocus();
                System.out.println("Please fill in a unique file name.");
                return;
            }
        }
        String oldFileName = File.getCurrentFile().getFileName();
        File.getCurrentFile().setFileName(fileNameTextfield.getText());
        File.getCurrentFile().setContainerType(containerChoiceBox.getSelectionModel().getSelectedItem());
        File.getCurrentFile().setWrapperType(wrapperChoiceBox.getSelectionModel().getSelectedItem());
        File.getCurrentFile().setViewport_width(Integer.parseInt(viewportWidthTextfield.getText()));
        File.getCurrentFile().setViewport_height(Integer.parseInt(viewportHeightTextfield.getText()));
        if(dBhandler.updateFile(componentsDB, fileNameTextfield.getText(), oldFileName)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("File updated successfully.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File could not be updated.");
            alert.showAndWait();
        }
        stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    public void onCancelButtonClicked(){
        stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
