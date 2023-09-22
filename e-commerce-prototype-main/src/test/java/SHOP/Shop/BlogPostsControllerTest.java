package SHOP.Shop;

import SHOP.Logic.BlogPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BlogPostsControllerTest {
    BlogPostsController blogPostsController;
    @BeforeEach
    void setUp() {
        blogPostsController = new BlogPostsController();
    }
    @Test
    void fileToObject(){
//        assertInstanceOf(BlogPost.class,blogPostsController.fileToObject(new File("src/main/resources/SHOP/Files/FileB")));
    }
}