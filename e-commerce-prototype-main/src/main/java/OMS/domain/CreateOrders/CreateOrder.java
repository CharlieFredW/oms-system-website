package OMS.domain.CreateOrders;

import OMS.UI.OtherGroupsAPIS.ICreateOrder;
import OMS.domain.OrderCRUD.OrderCRUD;
import OMS.domain.OrderPacker.OrderPacker;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateOrder implements ICreateOrder {

    ArrayList<OrderProducts> productsList;
    Order order;

    //creating method to create an order
    @Override
    public void CreateOrder(JSONObject input) {
        if (input != null) {
            createOrder(input);
            createProducts(input);
        }
    }

    //creating orders from a string line
    private Order createOrder(JSONObject input) {

        int zip = input.getJSONObject("customer").getInt("zip");
        String country = input.getJSONObject("customer").getString("country");
        String address = input.getJSONObject("customer").getString("address");
        String mail = input.getJSONObject("customer").getString("mail");
        String city = input.getJSONObject("customer").getString("city");
        String name = input.getJSONObject("customer").getString("name");
        int telephone = input.getJSONObject("customer").getInt("telephone");
        AtomicInteger sum = new AtomicInteger();
        input.getJSONObject("cart").keys().forEachRemaining(key -> {
            sum.set(sum.get() + input.getJSONObject("cart").getJSONObject(key).getInt("price") * input.getJSONObject("cart").getJSONObject(key).getInt("amount"));
        });

        order = new Order(name, mail, telephone, address, zip, city, country, sum.get(), OrderStatus.PROCESSING, 20);
        boolean success = new OrderCRUD().createOrder(this);
        if (success) {
            new OrderPacker().updateOrderStatus(new OrderCRUD().getNewestOrderId(), OrderStatus.PROCESSED);
            return order;
        } else {
            return null;
        }
    }

    //creating products from lines of strings
    private ArrayList<OrderProducts> createProducts(JSONObject input) {
        productsList = new ArrayList<>();
        input.getJSONObject("cart").keys().forEachRemaining(key -> {
            int amount = input.getJSONObject("cart").getJSONObject(key).getInt("amount");
            int price = input.getJSONObject("cart").getJSONObject(key).getInt("price");
            int id = input.getJSONObject("cart").getJSONObject(key).getInt("id");
            productsList.add(new OrderProducts(id, amount, price));
        });

        boolean success = new OrderCRUD().createProduct(this);
        if (success) {
            return productsList;
        } else {
            return null;
        }
    }

    public ArrayList<OrderProducts> getProductsList() {
        return productsList;
    }

    public Order getOrder() {
        return order;
    }

}
