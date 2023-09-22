package SHOP.Logic;

import java.util.ArrayList;

public interface IpimHandler {
    public ArrayList<Product> getAllProducts();
    public ArrayList<Product> getProductsByCategory(String string);
    public ArrayList<Product> getProductsByTitle(String string);
    public Product getProduct(String id);
}