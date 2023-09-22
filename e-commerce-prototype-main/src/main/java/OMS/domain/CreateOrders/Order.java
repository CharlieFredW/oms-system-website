package OMS.domain.CreateOrders;

import java.sql.Timestamp;

public class Order {

    private int orderNumber;
    private final String fullName;
    private final String eMail;
    private final int phoneNumber;
    private final String address;
    private final int zipCode;
    private final String city;
    private final String country;
    private final double totalPrice;
    private final double vat;
    private final OrderStatus status;
    private Timestamp orderDate;


    public Order(String fullName, String eMail, int phoneNumber, String address, int zipCode, String city, String country, double totalPrice, OrderStatus status, double vat) {
        this.fullName = fullName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.totalPrice = totalPrice;
        this.status = status;
        this.vat = vat;
    }

    public Order(int orderNumber, String fullName, String eMail, int phoneNumber, String address, int zipCode, String city, String country, Timestamp orderDate, double totalPrice, OrderStatus status, double vat) {
        this.orderNumber = orderNumber;
        this.fullName = fullName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.vat = vat;
    }

    public String toString() {
        return "Order: " + "\n" + " Full Name: " + getFullName() + "\n" + " eMail: " + getMail() + "\n" + " Phone Number: " + getPhoneNumber() + "\n" + " Address: " + getAddress() + "\n" + " Zipcode: " + getZipCode() + "\n" + " City: " + getCity() + "\n" + " Total price: " + getTotalPrice() + "\n" + " Status: " + getStatus() + "\n" + " Vat: " + getVat() + "\n";
    }

    public String getFullName() {
        return fullName;
    }

    public String getMail() {
        return eMail;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getVat() {
        return vat;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

}
