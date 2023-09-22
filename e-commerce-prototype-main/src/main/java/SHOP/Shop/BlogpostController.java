package SHOP.Shop;

import SHOP.Logic.BlogPost;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class BlogpostController implements Controller {
    @FXML
    Label title, body;

    public void showBlogPost(BlogPost blogPost){
        MainController.getInstance().setMainWindow("blog_post");
        ((BlogpostController)MainController.getInstance().getCurrentController()).updatePost(blogPost);
    }

    public void updatePost(BlogPost blogPost){
        this.title.setText(blogPost.getTitle());
        this.body.setText(blogPost.getBody());
    }

}