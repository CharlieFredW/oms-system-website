package CMS.Logic;

import CMS.Database.DBhandler;
import CMS.Database.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Load_Controller implements Initializable {
    /* DB */
    private final DataBase componentsDB = new DataBase(
            "db.kxzcqlftwnjqgfkpahvp.supabase.co",
            5432,
            "postgres",
            "postgres",
            "xpx2NfdK2Q865FKf"
    );

    private DBhandler dBhandler = new DBhandler();
    ResultSet rsLoad;
    Stage stage;
    @FXML
    ListView<File> loadListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLoadListView();
    }
    @FXML
    private void onOpenButtonClicked(){
        if(!loadListView.getSelectionModel().isEmpty()){
            File.setIsOpening(true);
            File.setCurrentFile(loadListView.getSelectionModel().getSelectedItem());
            stage = (Stage) loadListView.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    private void onNewButtonClicked(){
        // TODO
    }
    @FXML
    private void onDeleteButtonClicked(){
        if(!loadListView.getSelectionModel().isEmpty()){
            String target = loadListView.getSelectionModel().getSelectedItem().getFileName();
            if(File.getCurrentFile() != null) { if(File.getCurrentFile().getFileName().equals(target)){ // Prevents deleting the current file
                displayError("Cannot delete the current file.\nPlease close the file before deleting it.");
                return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete \'" + target + "\'?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.NO || alert.getResult() == ButtonType.CANCEL) return;
            dBhandler.deleteFile(componentsDB, loadListView.getSelectionModel().getSelectedItem().getFileName());
            updateLoadListView();
        } else {
            displayError("Please select a file to delete.");
        }
    }
    @FXML
    private void onCancelButtonClicked(){
        stage = (Stage) loadListView.getScene().getWindow();
        stage.close();
    }

    private void updateLoadListView(){
        loadListView.getItems().clear();
        rsLoad = dBhandler.getAllFromDB(componentsDB, "files");
        try {
            //rs.first();
            if(!rsLoad.isBeforeFirst())  {
                System.out.println("No files in database.");
                return;
            }
            while(rsLoad.isBeforeFirst())  {
                rsLoad.next();
            }

            while(!rsLoad.isAfterLast()) {
                String name = rsLoad.getString("filename");
                String container = rsLoad.getString("containerType");
                String wrapper = rsLoad.getString("wrapperType");
                int viewport_width = rsLoad.getInt("viewport_width");
                int viewport_height = rsLoad.getInt("viewport_height");

                loadListView.getItems().add(new File(name, container, wrapper, viewport_width, viewport_height));

                rsLoad.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void displayError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.showAndWait();
    }
}
