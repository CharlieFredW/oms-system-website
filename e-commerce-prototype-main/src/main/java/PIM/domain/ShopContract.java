package PIM.domain;

import org.json.JSONObject;

public interface ShopContract{
    public JSONObject getAllProducts();
    public JSONObject getProductsByTags(String string);
    public JSONObject getProductsByName(String string);

}
