package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DemoController {
    @FXML
    Stage stage;
    Scene scene;
    CMSHandler cmsHandler = new CMSHandler();

    public void showNewScene(ActionEvent actionEvent) throws IOException {
        Node node = ((Node) actionEvent.getSource()).getScene().getFocusOwner();
        String id = node.getId();
        String directory = "";
        switch (id) {
            case "pc":
                directory = "scene2";
                break;
            case "cpu":
                directory = "scene3";
                break;
            case "start":
                directory = "scene1";
                break;
            case "rabbit":
                directory = "scene4";
                break;
        }
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(cmsHandler.getPage(directory).load());
        stage.setScene(scene);
        stage.show();
    }

}
