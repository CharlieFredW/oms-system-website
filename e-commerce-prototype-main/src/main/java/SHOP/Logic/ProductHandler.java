package SHOP.Logic;

import PIM.domain.SHOPtoPIMHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProductHandler {

    ArrayList<Product> products = new ArrayList<>();
    public ArrayList<Product> getProductList(boolean usePim) {
        ArrayList<Product> products = new ArrayList<>();
        if (!usePim) {
            return readProductsFromFile("src/main/resources/SHOP/products.txt");
        } else {
            JSONObject allProductsJSON = SHOPtoPIMHandler.getInstance().getAllProducts();
            JSONArray jsonArray = allProductsJSON.getJSONArray("product");
            for(int i = 0; i < jsonArray.length()-1; i++){
                JSONObject productJson = jsonArray.getJSONObject(i);

                Product product = new Product(productJson.getString("product_id"),
                        productJson.getString("category"),
                        Double.parseDouble(productJson.get("price").toString()),
                        productJson.getString("name"),
                        productJson.getString("description"),
                        productJson.getString("color"),
                        "",
                        productJson.getString("image"),
                        Integer.parseInt(productJson.get("stock").toString()));
                products.add(product);

            }
        this.products = products;
        } return products;
    }





    public static ArrayList<Product> readProductsFromFile(String fileName) {
        ArrayList<Product> allProducts = new ArrayList<Product>();
        String line = "";
        String delimiter = ";";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);
                String ID = fields[0];
                String category = fields[1];
                double price = Double.parseDouble(fields[2]);
                String title = fields[3];
                String description = fields[4];
                String color = fields[5];
                String tag = fields[6];
                String imageDirectory = fields[7];
                allProducts.add(new Product(ID, category, price, title, description, color, tag, imageDirectory,10));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }
}
