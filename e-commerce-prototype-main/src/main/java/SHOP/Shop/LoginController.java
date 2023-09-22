package SHOP.Shop;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LoginController implements Controller{

    @FXML
    private Button newCustomerBtn, existingCustomerBtn, guestBtn;
    @FXML
    private Label textLabel;

    public void checkoutLogin(){
        textLabel.setText("Continue as");
        guestBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MainController.getInstance().getCheckoutController().checkoutInfo();
            }
        });
    }
    public void login(){
        textLabel.setText("Login");
        guestBtn.setVisible(false);
    }
}
