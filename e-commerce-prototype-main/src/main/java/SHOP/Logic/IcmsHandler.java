package SHOP.Logic;

import javafx.fxml.FXMLLoader;
import org.json.JSONObject;

import java.io.File;

public interface IcmsHandler {
    public FXMLLoader getPage(String name);
    public File getPosts();
}
