package PIM.domain;
import org.json.*;

import java.util.List;

public class SHOPtoPIMHandler implements ShopContract {
    private static SHOPtoPIMHandler instance;
    private JSONObject createProductsJson(List<Product> productList, String jsonKey) {
            JSONObject allProducts = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            productList.forEach((product) -> {
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("product_id", product.getProduct_id());
                jsonProduct.put("name", product.getName());
                jsonProduct.put("description", product.getDescription());
                jsonProduct.put("color", product.getColor());
                jsonProduct.put("stock", product.getStock());
                jsonProduct.put("category", product.getCategory());
                jsonProduct.put("price", product.getPrice());
                jsonProduct.put("discount", product.getDiscount());
                jsonProduct.put("image", product.getStockImage());
                jsonArray.put(jsonProduct);
            });
            allProducts.put(jsonKey, jsonArray);
            return allProducts;
    }
    public JSONObject getAllProducts() {
        List<Product> productList = PersistenceHandler.getInstance().findAllProducts();
        return createProductsJson(productList, "product");
    }

    @Override
    public JSONObject getProductsByTags(String string) {
        try {
            List<Product> productList = PersistenceHandler.getInstance().searchProductsByTag(string);
            return createProductsJson(productList, "product");
        } catch(NullPointerException e) {
            System.out.println("Nullpointer Exception in getProductsByTags");
            return null;
        }
    }

    @Override
    public JSONObject getProductsByName(String string) {
        List<Product> productList = PersistenceHandler.getInstance().searchProductsByName(string, 3);
        return createProductsJson(productList, "product");
    }

    public static SHOPtoPIMHandler getInstance() {
        if (instance == null) {
            instance = new SHOPtoPIMHandler();
        }
        return instance;
    }
}
