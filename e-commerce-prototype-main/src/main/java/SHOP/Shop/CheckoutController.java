package SHOP.Shop;

import SHOP.Logic.Order;

public class CheckoutController {

    private Order order;

    public void checkoutLogin(){
        if(!MainController.getInstance().getShoppingCart().getCart().isEmpty()) {
            LoginController loginController = (LoginController) MainController.getInstance().setMainWindow("login_page");
            loginController.checkoutLogin();
        }
    }

    public void checkoutInfo(){
        order = new Order(MainController.getInstance().getShoppingCart());
        CheckoutInfoController checkoutInfoController = (CheckoutInfoController) MainController.getInstance().setMainWindow("order_page_infoo");
        checkoutInfoController.setOnAction();
    }

    public void checkoutBilling(){
        CheckoutBillingController checkoutBillingController = (CheckoutBillingController) MainController.getInstance().setMainWindow("order_page_billing");
        checkoutBillingController.updateBilling();
        checkoutBillingController.updatePayBtn();
    }

    public Order getOrder(){
        return order;
    }
    public void setOrder(Order order){
        this.order = order;
    }
}
