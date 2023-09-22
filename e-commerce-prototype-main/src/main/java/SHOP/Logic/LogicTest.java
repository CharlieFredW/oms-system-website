package SHOP.Logic;

import PIM.domain.SHOPtoPIMHandler;

public class LogicTest {

    public static void main(String[] args) {
        /*
        Order order = new Order("Lasse","lahvi22@student.sdu.dk","42414339","Glanshatten","5220","OdenseSØ","Denmark");
        order.cart = new ShoppingCart();

        System.out.println("Tilføjer 3 product objekter til orders cart-list");
        Product p1 = new Product("1","cpu",250);
        Product p2 = new Product("2","cpu",500);
        Product p3 = new Product("3","pc",250);
        Product p4 = new Product("4","pc",250);
        order.cart.cartAdd(p1,1);
        order.cart.cartAdd(p2,2);
        order.cart.cartAdd(p3,3);
        order.cart.cartAdd(p4,4);

        System.out.println(order);
        System.out.println("Total Price: " + order.cart.updateTotalPrice());
        System.out.println("VAT: " + order.cart.calculateVAT());


        System.out.println("___________________________" + "\n Slettede Produkter på ID 2");
        order.cart.removeProductID("2");
        System.out.println(order.cart);
        System.out.println("Total Price: " + order.cart.updateTotalPrice());
        System.out.println("VAT: " + order.cart.calculateVAT());


        System.out.println("___________________________" + "\n Increment amount på ID 3");
        order.cart.incrementItem("3");
        System.out.println(order.cart);
        System.out.println("Total Price: " + order.cart.updateTotalPrice());
        System.out.println("VAT: " + order.cart.calculateVAT());


        System.out.println("___________________________" + "\n Decrement amount på ID 3");
        order.cart.decrementItem("3");
        System.out.println(order.cart);
        System.out.println("Total Price: " + order.cart.updateTotalPrice());
        System.out.println("VAT: " + order.cart.calculateVAT());


        System.out.println("___________________________" + "\n JSON Print");
        System.out.println(order.toJSON());


        System.out.println("___________________________" + "\n Clear Cart");
        order.cart.clearCart();
        System.out.println(order.cart);
        System.out.println("Total Price: " + order.cart.updateTotalPrice());
        System.out.println("VAT: " + order.cart.calculateVAT());

         */
        
        System.out.println(SHOPtoPIMHandler.getInstance().getAllProducts());
    }
}
