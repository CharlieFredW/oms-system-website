package SHOP.Logic;

import SHOP.Shop.MainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    ArrayList<Product> list;
    Product phone = new Product("1","Phone","iphone4");
    Product mouse = new Product("1","mouse","mouse2");
    Product cpu = new Product("1","cpu","cpu3");

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();

        list.add(phone);
        list.add(mouse);
        list.add(cpu);
    }

    @Test
    void getPhones() {
        ArrayList<Product> results = new ArrayList<>();
        for(Product product : list){
            if(product.getCategory().equalsIgnoreCase("Phone")){
                results.add(product);
            }
        }
        assertTrue(results.contains(phone));
        assertFalse(results.contains(mouse));
        assertFalse(results.contains(cpu));
    }

    @Test
    void getMouse() {
        ArrayList<Product> results = new ArrayList<>();
        for(Product product : list){
            if(product.getCategory().equalsIgnoreCase("mouse")){
                results.add(product);
            }
        }
        assertTrue(results.contains(mouse));
        assertFalse(results.contains(phone));
        assertFalse(results.contains(cpu));
    }

    @Test
    void getCPU() {
        ArrayList<Product> results = new ArrayList<>();
        for(Product product : list){
            if(product.getCategory().equalsIgnoreCase("cpu")){
                results.add(product);
            }
        }
        assertTrue(results.contains(cpu));
        assertFalse(results.contains(phone));
        assertFalse(results.contains(mouse));
    }

    @Test
    void getSearchResults() {
        ArrayList<Product> results = new ArrayList<>();
        String input = "iphone4";
        for(Product product : list){
            if(product.getCategory().equalsIgnoreCase(input)){
                results.add(product);
            }
            if(product.getTitle().equalsIgnoreCase(input)){
                results.add(product);
            }
        }
        assertTrue(results.contains(phone));
        assertFalse(results.contains(cpu));
        assertFalse(results.contains(mouse));
    }
}