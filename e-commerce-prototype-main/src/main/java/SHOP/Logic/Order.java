package SHOP.Logic;

import org.json.JSONObject;
import java.util.Map;

public class Order {
    private ShoppingCart cart;
    private String  name,
                    mail,
                    telephone,
                    street,
                    streetNumber,
                    floor,
                    door,
                    zip,
                    city,
                    country;
    private double  totalPrice,
                    vat;

    public Order(ShoppingCart cart){
        this.cart = cart;
    }

    public Order(String name,String mail,String telephone,String address,String zip,String city,String country, ShoppingCart cart) {
        this.cart = cart;
        this.name = name;
        this.mail = mail;
        this.telephone = telephone;
        this.street = address;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }
    public Order(String name,String mail,String telephone,String address,String zip,String city,String country) {
        this.name = name;
        this.mail = mail;
        this.telephone = telephone;
        this.street = address;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }
    @Override
    public String toString() {
        totalPrice = cart.updateTotalPrice();
        vat = cart.calculateVAT();
        String personInfo = "name: " + name + "\n" +"mail: " + mail + "\n" + "telephone: " + telephone + "\n"
                + "country: " + country + "\n" + "city: " + city + "\n" + "zip: " + zip + "\n" + "street: " + street + "\n"
                + "total price: " + totalPrice + "\n" + "vat " + vat + "\n";
        String productInfo = "";
        for (Map.Entry<Product,Integer> entry : cart.getCart().entrySet()) {
            productInfo += entry.toString() + "\n";
        }
        return personInfo + productInfo;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        JSONObject customer = new JSONObject();
        customer.put("name", name);
        customer.put("mail", mail);
        customer.put("telephone", telephone);
        customer.put("address", street);
        customer.put("zip", zip);
        customer.put("city", city);
        customer.put("country", country);
        json.put("customer", customer);

        JSONObject cart = new JSONObject();
        for (Map.Entry<Product,Integer> entry : this.cart.getCart().entrySet()) {
            JSONObject product = new JSONObject();
            product.put("id", entry.getKey().getID());
            product.put("title", entry.getKey().getTitle());
            product.put("price", entry.getKey().getPrice());
            product.put("amount", entry.getValue());
            cart.put(entry.getKey().getID(), product);
        }
        json.put("cart", cart);

        return json;
    }

    public ShoppingCart getCart() {
        return cart;
    }
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public double getVat() {
        return vat;
    }
    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }
}
