package SHOP.Shop;

import SHOP.Logic.OMSHandler;
import SHOP.Logic.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.Map;

public class CheckoutBillingController implements Controller{

    @FXML
    private Label totalPriceLabel;
    @FXML
    private TextField cardNumber, cartHolderName, expirationMonth, expirationYear, cvv;
    @FXML
    private Button payBtn;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void updateBilling(){
        totalPriceLabel.setText(df.format(MainController.getInstance().getShoppingCart().getTotalPrice())+",- DKK");
    }

    public void updatePayBtn(){
        payBtn.setOnMouseClicked(mouseEvent -> {
            boolean isInStock = true;
            for (Map.Entry<Product, Integer> entry : MainController.getInstance().getShoppingCart().getCart().entrySet()){
                if(!(entry.getKey().getStock() >= entry.getValue())){
                    isInStock = false;
                    System.out.println(entry.getKey().getTitle() + " is not in stock");
                }
            }
            if(isInStock){
                CheckoutCompleteController checkoutCompleteController = (CheckoutCompleteController) MainController.getInstance().setMainWindow("order_page_complete");
                MainController.getInstance().getShoppingCart().clearCart();
                //System.out.println(MainController.getInstance().getShoppingCart());
                OMSHandler omsHandler = new OMSHandler();
                omsHandler.placeOrder(MainController.getInstance().getCheckoutController().getOrder());
            }
        });
    }

}
