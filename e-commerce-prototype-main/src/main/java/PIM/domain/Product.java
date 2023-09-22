package PIM.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product {

    private final String product_id;
    private String name, description, color, category;
    private int stock;
    private double price, discount;
    private ProductHistory productHistory;
    private String stockImage = "src/main/resources/SHOP/Icons/product_dummy.jpg";
    private ArrayList<Tag> tagArrayList;

    // Constructor for when a brand-new product is added to the database
    public Product(String name, String description, String color, int stock, String category, double price, double discount) {
        this.product_id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.color = color;
        this.stock = stock;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.tagArrayList = new ArrayList<>();
        this.productHistory = new ProductHistory(this.product_id);
    }

    public Product(String product_id, String name, String color, double price, int stock,String description, String category) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.color = color;
        this.stock = stock;
        this.price = price;

    }

    public Product(String product_id, String name) {
        this.product_id = product_id;
        this.name = name;
    }
    
    public Product(String product_id, String name, String color, double price, int stock) {
        this.product_id = product_id;
        this.name = name;
        this.color = color;
        this.price = price;
        this.stock = stock;
    }

    // Constructor for when a product is fetched from the database
    public Product(String productID, String name, String description, String color, int stock,
                   String category, double price, double discount) {
        this.product_id = productID;
        this.name = name;
        this.description = description;
        this.color = color;
        this.stock = stock;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.tagArrayList = new ArrayList<>();
        this.productHistory = PersistenceHandler.getInstance().findProductHistoryByProduct(this);

        populateTagArrayList(this);
    }

    public void populateTagArrayList(Product product) {
        List<ProductTagPairing> productTagPairings =
                PersistenceHandler.getInstance().findProductTagPairingsByProduct(product.getProduct_id());

        for (ProductTagPairing productTagPairing : productTagPairings) {
            product.tagArrayList.add(
                    PersistenceHandler.getInstance().findTagByID(productTagPairing.getTag_id())
            );
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public int getStock() {
        return stock;
    }

    public String getStockImage() {
        return stockImage;
    }

    public void setStockImage(String stockImage) {
        this.stockImage = stockImage;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public ArrayList<Tag> getProductTags() {
        return tagArrayList;
    }

    public ProductHistory getProductHistory() {
        return productHistory;
    }

    public void addTagToProduct(Tag tag) {
        tagArrayList.add(tag);
        PersistenceHandler.getInstance().addProductTagPairing(new ProductTagPairing(this.product_id, tag.getTag_id()));
    }
}

