package SHOP.Shop;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.event.HyperlinkEvent;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    @BeforeEach
    void setUp() throws InterruptedException{
        ShopApplication.launch(ShopApplication.class);
        boolean loaded = false;
        while (!loaded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (MainController.getInstance().getProductPane().getChildren().get(200) != null) {
                loaded = true;
            }
        }
        showHomePage();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void showHomePage() throws InterruptedException {
        // Create a test that checks if the homepage is loaded in less than 1 second

        long start = System.currentTimeMillis();
        Thread thread = MainController.getInstance().productLoadingThread();
        thread.start();
        thread.join();
        long end = System.currentTimeMillis();

        System.out.println("showHomePage: " + (end-start));
    }
}