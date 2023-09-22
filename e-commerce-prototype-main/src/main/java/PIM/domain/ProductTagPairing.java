package PIM.domain;

public class ProductTagPairing {
    private int pairing_id;
    private String tag_id;
    private String product_id;

    public ProductTagPairing(String product_id, String tag_id) {
        this.product_id = product_id;
        this.tag_id = tag_id;
    }

    public int getPairing_id() {
        return pairing_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public String getProduct_id() {
        return product_id;
    }
}
