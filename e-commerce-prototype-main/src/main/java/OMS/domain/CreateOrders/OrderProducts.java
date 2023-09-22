package OMS.domain.CreateOrders;

public class OrderProducts {

    private final int productId;
    private final int amount;
    private final double price;

    public OrderProducts(int productId, int amount, double price) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;

    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Order Products: " + "\n" + " productId: " + productId + "\n" + " amount: " + amount + "\n" + " price: " + price + "\n";
    }
}
