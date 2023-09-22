package SHOP.Logic;

import java.util.*;

public class ShoppingCart {
    private HashMap<Product, Integer> cart;
    private double totalPrice;

    public ShoppingCart() {
        this.cart = new HashMap<>();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    Iterator<Map.Entry<Product,Integer>> iterator;

    public void cartAdd(Product product, int amount) {
        if(!cart.containsKey(product)){
            cart.put(product,amount);
        }
        else {
            int value = cart.get(product);
            value += amount;
            cart.put(product,value);
        }
        updateTotalPrice();
    }
    
    public void removeProductID(String ID) {
        iterator = cart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Product,Integer> entry = iterator.next();
            if (entry.getKey().getID().equals(ID)) {
                iterator.remove();
            }
        }
        updateTotalPrice();
    }
    public void incrementItem(String ID) {
        iterator = cart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Product,Integer> entry = iterator.next();
            if (entry.getKey().getID().equals(ID)) {
                if (entry.getValue() >= 1) {
                    cart.put(entry.getKey(),entry.getValue()+1);
                }
            }
        }
        updateTotalPrice();
    }
    public void decrementItem(String ID) {
        iterator = cart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Product,Integer> entry = iterator.next();
            if (entry.getKey().getID().equals(ID)) {
                if (entry.getValue() >= 1) {
                    cart.put(entry.getKey(),entry.getValue()-1);
                }
            }
        }
        updateTotalPrice();
    }
    public void clearCart() {
        cart.clear();
        totalPrice = 0;
    }
    public double updateTotalPrice() {
        totalPrice = 0;
        cart.forEach(
                (key,value) -> totalPrice += key.getPrice()*value);
        return totalPrice;
    }
    public double calculateVAT(){
        return  totalPrice/4 ;
    }
    public HashMap<Product, Integer> getCart() {
        return cart;
    }
    @Override
    public String toString() {
        String products = "";
        for (Map.Entry<Product,Integer> entry : cart.entrySet()) {
            products += entry.getKey() + ",amount=" + entry.getValue() + "\n";
        }
        return products;
    }
}
