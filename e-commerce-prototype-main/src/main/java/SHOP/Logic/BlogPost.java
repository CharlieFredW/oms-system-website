package SHOP.Logic;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class BlogPost {
    String title;
    String body;
    Image image;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Image getImage() {
        return image;
    }

    public BlogPost(String title, String body, Image image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }
    public BlogPost(String title, String body){
        this.title = title;
        this.body = body;
    }
    @Override
    public String toString(){
        return title;
    }
}
