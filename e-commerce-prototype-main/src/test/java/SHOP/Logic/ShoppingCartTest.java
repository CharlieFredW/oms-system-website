package SHOP.Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    ShoppingCart shoppingCart;
    Product product;
    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
        product = new Product("1","cpu",500);
    }
    @Test
    void incrementItem() {
        shoppingCart.cartAdd(product,3);
        shoppingCart.incrementItem("1");
        assertEquals(4,shoppingCart.getCart().get(product));
    }
    @Test
    void decrementItem() {
        shoppingCart.cartAdd(product,3);
        shoppingCart.decrementItem("1");
        assertEquals(2,shoppingCart.getCart().get(product));
    }
    @Test
    void calculateVAT(){
        shoppingCart.cartAdd(product,1);
        assertEquals(125.,shoppingCart.calculateVAT());
    }
    @Test
    void addProduct(){
        shoppingCart.cartAdd(product,3);
        assertEquals(3,shoppingCart.getCart().get(product));
    }
    @Test
    void removeId(){
        shoppingCart.cartAdd(product,3);
        shoppingCart.removeProductID("1");
        assertTrue(shoppingCart.getCart().isEmpty());
    }
}