package SHOP.Shop;

import SHOP.Logic.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderController{
    @FXML
    TextField fName, lName, phone,mail, street, zip, city, adress_number, floor,contry;
    @FXML
    TextField card_number, cardholder_name, expiration_month, expiration_year, cvv;
    @FXML
    Stage stage;
    Scene scene;
    CMSHandler cmsHandler = new CMSHandler();
    OMSHandler omsHandler = new OMSHandler();
    public void showPage(ScrollPane scrollPane){
        try {
            FXMLLoader loader = cmsHandler.getPage("order_page_Info");
            VBox orderInfoPage = loader.load();
//            ProductPageDataController productPageDataController = loader.getController();
//            productPageDataController.updateProductPage(product,mainController);
            scrollPane.setContent(orderInfoPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showNewScene(ActionEvent actionEvent) throws IOException {
        Node node = ((Node) actionEvent.getSource()).getScene().getFocusOwner();
        String id = node.getId();
        String directory = "";
        switch (id) {
            case "process_order":
                if (checkShippingInputs()) {
                    directory = "order_page_billing";
                    omsHandler.placeOrder(new Order(
                            fName.getText() +" "+ lName.getText(), mail.getText(), phone.getText(),
                            street.getText() +" " + adress_number.getText(), zip.getText(),
                            city.getText(), contry.getText(),MainController.getInstance().getShoppingCart()));
                }
                break;
            case "pay":
                if (checkBillingInputs()){
                    directory = "landing_page";
                }
                break;
        }
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(cmsHandler.getPage(directory).load());
        stage.setScene(scene);
        stage.show();
    }

    public boolean checkShippingInputs(){
        try{
            boolean r1 = Pattern.matches("^[0-9]{8}$",phone.getText());
            boolean r2 = Pattern.matches("^\\d+$",zip.getText());
            boolean r3 = Pattern.matches("^\\d+$",adress_number.getText());
            boolean r4 = Pattern.matches("^[a-å]+$",fName.getText());
            boolean r5 = Pattern.matches("^[a-å]+$",lName.getText());
            boolean r6 = Pattern.matches("^[a-å]+$",contry.getText());
            boolean r7 = Pattern.matches("^[a-å]+$]",city.getText());
            boolean r8 = Pattern.matches("^[a-å]+$",street.getText());
            boolean r9 = Pattern.matches("^(?=.*\\w)(?=.*@).*$.",mail.getText());
            boolean r10 = false;
            if (floor.getText().isEmpty()){
                r10 = true;
            } else if (!floor.getText().isEmpty()) {
                r10 = Pattern.matches("[0-9]",floor.getText());
            }
            if (r1&&r2&&r3&&r4&&r5&&r6&&r7&&r8&&r9&&r10){
                return true;
            }
            else throw new InvalidInputException();
        }
        catch (InvalidInputException e){
//            fName.setText("");
//            lName.setText("");
//            phone.setText("");
//            mail.setText("");
//            street.setText("");

            return false;
        }
    }

    public boolean checkBillingInputs(){
        try{
            boolean r1 = Pattern.matches("^[0-9]{3}$",cvv.getText());
            boolean r2 = Pattern.matches("^[0-9]{16}$",card_number.getText());
            boolean r3 = Pattern.matches("^[0-9]{2}$",expiration_month.getText());
            boolean r4 = Pattern.matches("^[0-9]{2}$",expiration_year.getText());
            boolean r5 = Pattern.matches("^[\\w]+$", cardholder_name.getText());
            if(r1&&r2&&r3&&r4&&r5){
                return true;
            }
            else throw new InvalidInputException();
        }
        catch (InvalidInputException e){
//            cardholder_name.setText("");
//            cvv.setText("");
//            expiration_year.setText("");
//            expiration_month.setText("");
//            card_number.setText("");
            return false;
        }
    }
}
