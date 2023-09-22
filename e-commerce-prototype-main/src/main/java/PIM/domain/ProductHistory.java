package PIM.domain;

import java.sql.Date;
import java.util.UUID;

public class ProductHistory {

    private String product_history_id;
    private Date lastSale;
    private Date createdOn;
    private String relatedProductID;

    // Constructor for creating a brand-new ProductHistory to add to the database
    public ProductHistory(String relatedProductID) {
        this.product_history_id = String.valueOf(UUID.randomUUID());;
        this.createdOn = new Date(System.currentTimeMillis());
        this.relatedProductID = relatedProductID;
    }

    // Constructor for fetching a ProductHistory from the database
    public ProductHistory(String productHistoryId, Date lastSale, Date createdOn, String relatedProductID) {
        this.product_history_id = productHistoryId;
        this.lastSale = lastSale;
        this.createdOn = createdOn;
        this.relatedProductID = relatedProductID;
    }

    public String getProduct_history_id() {
        return product_history_id;
    }
    public Date getLastSale() {
        return lastSale;
    }
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setLastSale(Date lastSale) {
        this.lastSale = lastSale;
    }
}
