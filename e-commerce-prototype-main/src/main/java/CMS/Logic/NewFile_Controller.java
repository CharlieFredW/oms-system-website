package CMS.Logic;

import CMS.Database.DBhandler;
import CMS.Database.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewFile_Controller implements Initializable {
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
    Button createButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessageHandler = new ErrorMessageHandler(errorMessageLabel, 5, "red");
        containerChoiceBox.getItems().addAll("AnchorPane", "BorderPane", "FlowPane", "StackPane", "ScrollPane");
        wrapperChoiceBox.getItems().addAll("None", "HBox", "VBox", "HBox+VBox", "VBox+HBox");
    }

    public void onCreateButtonClicked() {
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
        if(wrapperChoiceBox.getSelectionModel().isEmpty()) {
            errorMessageHandler.displayError("Please select a wrapper.");
            wrapperChoiceBox.requestFocus();
            System.out.println("Please select a wrapper.");
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
        if(!File.isUniqueFileName(fileNameTextfield.getText(), componentsDB)){
            errorMessageHandler.displayError("A file with this name already exists.");
            fileNameTextfield.requestFocus();
            System.out.println("Please fill in a unique file name.");
            return;
        }

        File.setCurrentFile(new File(
                fileNameTextfield.getText(),
                containerChoiceBox.getSelectionModel().getSelectedItem(),
                wrapperChoiceBox.getSelectionModel().getSelectedItem(),
                Integer.parseInt(viewportWidthTextfield.getText()),
                Integer.parseInt(viewportHeightTextfield.getText())
        ));
        dBhandler.addNewFile(componentsDB, File.getCurrentFile());
        File.setIsOpening(true);
        stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
    public void onCancelButtonClicked(){
        stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
