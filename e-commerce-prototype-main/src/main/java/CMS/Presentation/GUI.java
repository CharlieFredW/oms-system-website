package CMS.Presentation;

import CMS.Logic.GUI_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {
    private static Stage primaryStage;
    private static GUI_Controller primaryController;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("GUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        primaryController = fxmlLoader.getController();


        //Add keyevent handlers to the scene, both modifiers and keys.
        scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, KeyEvent -> {
            primaryController.keyPressHandler(KeyEvent);
        });
        scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, KeyEvent -> {
            primaryController.keyModifierHandler(KeyEvent);
        });

        //Remove focus from the textfields, so that the keyevents are sent to the scene.
        primaryController.removeFocus();

        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setTitle("Content Management System");
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() { return primaryStage; }
    public static  GUI_Controller getPrimaryController() { return primaryController; }
    public void setStageTitle(String newTitle) { primaryStage.setTitle(newTitle); }
}