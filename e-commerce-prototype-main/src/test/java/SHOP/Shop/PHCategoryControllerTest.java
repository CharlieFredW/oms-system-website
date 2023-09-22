package SHOP.Shop;

import SHOP.Logic.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PHCategoryControllerTest {
    ArrayList<Product> list;
    Product phone1 = new Product("1","Phone",2000);
    Product phone2 = new Product("1","Phone",5000);
    Product phone3 = new Product("1","Phone",300);

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();

        list.add(phone1);
        list.add(phone2);
        list.add(phone3);
    }

    @Test
    void displayProducts() {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        Double minPrice = 200.0;
        Double maxPrice = 3000.;
        for(Product product : list){
            if(product.getCategory().equalsIgnoreCase("phone")){
                if(product.getPrice() >= minPrice &&
                        product.getPrice() <= maxPrice) {
                    filteredProducts.add(product);
                }
            }
        }
        assertTrue(filteredProducts.contains(phone1));
        assertTrue(filteredProducts.contains(phone3));
        assertFalse(filteredProducts.contains(phone2));
    }
}