package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ShopApplication extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D userScreen = Screen.getPrimary().getBounds();
        System.out.println(userScreen.getMaxX() + " " + userScreen.getMaxY());
        CMSHandler cmsHandler = new CMSHandler();
        FXMLLoader fxmlLoader = cmsHandler.getPage("landing_page");
        fxmlLoader.setController(MainController.getInstance());
        Scene scene = new Scene(fxmlLoader.load(), 886,768);
        MainController.getInstance().setScene(scene);
        MainController.getInstance().showHomePage();
        stage.setTitle("Arne's Electronic Shop");
        stage.setScene(scene);
        stage.show();
    }
}

