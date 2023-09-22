package SHOP.Logic;

import SHOP.Shop.ShopApplication;
import javafx.fxml.FXMLLoader;
import org.json.JSONObject;

import java.io.File;

public class CMSHandler implements IcmsHandler{
    @Override
    public FXMLLoader getPage(String name) {

        FXMLLoader fxmlLoader = new FXMLLoader(ShopApplication.class.getResource(name+".fxml"));
        return fxmlLoader;
    }

    @Override
    public File getPosts() {
        return new File("src/main/resources/SHOP/Files/FileA.json");
    }

}
