package SHOP.Shop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CheckoutInfoController implements Controller{

    @FXML
    private TextField   fName,
            lName,
            phoneNumber,
            mail;
    @FXML
    private TextField   street,
            streetNumber,
            floor,
            zip,
            city,
            country;
    @FXML
    private Button      process_order;

    public void setOnAction(){
        process_order.setOnAction(actionEvent -> {
            MainController.getInstance().getCheckoutController().checkoutBilling();
                    customerInfoToOrder();
                }
        );
    }

    public void customerInfoToOrder(){
        MainController.getInstance().getCheckoutController().getOrder().setName(fName.getText() + " " + lName.getText());
        MainController.getInstance().getCheckoutController().getOrder().setTelephone(phoneNumber.getText());
        MainController.getInstance().getCheckoutController().getOrder().setMail(mail.getText());
        MainController.getInstance().getCheckoutController().getOrder().setStreet(street.getText());
        MainController.getInstance().getCheckoutController().getOrder().setStreetNumber(streetNumber.getText());
        MainController.getInstance().getCheckoutController().getOrder().setZip(zip.getText());
        MainController.getInstance().getCheckoutController().getOrder().setCity(city.getText());
        MainController.getInstance().getCheckoutController().getOrder().setCountry(country.getText());
        MainController.getInstance().getCheckoutController().getOrder().setCart(MainController.getInstance().getShoppingCart());
        System.out.println(MainController.getInstance().getCheckoutController().getOrder());
    }
}
