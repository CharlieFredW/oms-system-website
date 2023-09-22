package SHOP.Shop;

import SHOP.Logic.BlogPost;
import SHOP.Logic.CMSHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.*;


public class BlogPostsController implements Initializable, Controller{
    @FXML
    private ListView<BlogPost> list;
    @FXML
    private Label title, body;
    @FXML
    private VBox blogPostBox;
    ArrayList<BlogPost> blogPostList = new ArrayList<>();
    BlogpostController blogpostController = new BlogpostController();
    CMSHandler cmsHandler = new CMSHandler();

    public void showListView() {
        this.blogPostList = new ArrayList<>();
        MainController.getInstance().setMainWindow("blog_posts_overview");
        ((BlogPostsController)MainController.getInstance().getCurrentController()).getList().resize(MainController.getInstance().getScrollPane().getHvalue(),MainController.getInstance().getScrollPane().getVvalue());
    }

    private void loadJsonFile() {
        JSONParser parser = new JSONParser();
        try(FileReader fileReader = new FileReader(cmsHandler.getPosts())){

            Object obj = parser.parse(fileReader);
            JSONArray postsList = (JSONArray) obj;
            postsList.forEach(post -> parseEveryObj((JSONObject) post));
        }
        catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void parseEveryObj(JSONObject obj){
        JSONObject jObj = (JSONObject) obj.get("post");
        String id = (String) jObj.get("id");
        String header = (String) jObj.get("title");
        String content = (String) jObj.get("body");
        BlogPost demoPost = new BlogPost(header,content);
        blogPostList.add(demoPost);
    }
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadJsonFile();
        list.setItems(FXCollections.observableArrayList(this.blogPostList));

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BlogPost>() {
            @Override
            public void changed(ObservableValue<? extends BlogPost> observableValue, BlogPost blogPost, BlogPost t1) {
                BlogPost post = list.getSelectionModel().getSelectedItem();
                blogpostController.showBlogPost(post);
            }
        });
    }

    public ListView<BlogPost> getList() {
        return list;
    }
}
