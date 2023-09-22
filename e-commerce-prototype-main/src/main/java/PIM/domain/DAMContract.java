package PIM.domain;

import org.json.JSONObject;

public interface DAMContract {
    public JSONObject getFilesForProduct(String uuid);
    public boolean notifyDAMofNewProduct(JSONObject product);
    public boolean notifyDAMofDeleteProduct(JSONObject product);

}
