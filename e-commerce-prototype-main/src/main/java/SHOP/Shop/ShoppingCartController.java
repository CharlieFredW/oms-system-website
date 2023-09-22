package SHOP.Shop;

public class ShoppingCartController {

    private ShoppingCartFieldController shoppingCartFieldController;

    public void showShoppingCart() {
        shoppingCartFieldController = (ShoppingCartFieldController) MainController.getInstance().setMainWindow(
                "shopping_cart");
        shoppingCartFieldController.showShoppingCart(MainController.getInstance(),
                shoppingCartFieldController.getShoppingCartProducts());
    }

    public ShoppingCartFieldController getShoppingCartFieldController(){
        return shoppingCartFieldController;
    }
}