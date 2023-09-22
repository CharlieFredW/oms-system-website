package SHOP.Logic;

import org.json.JSONObject;

public class OMSHandler implements IomsHandler {
    @Override
    public void placeOrder(Order order){
        JSONObject orderJSON = order.toJSON();
        System.out.println(orderJSON);
    }
    @Override
    public void sendOrder(String order){

    }
}
