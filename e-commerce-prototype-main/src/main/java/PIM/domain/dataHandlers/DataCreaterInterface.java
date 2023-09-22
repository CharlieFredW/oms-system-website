package PIM.domain.dataHandlers;

import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.ProductTagPairing;
import PIM.domain.Tag;

public interface DataCreaterInterface {
    public boolean addProduct(Product product);
    public boolean addTag(Tag tag);
    public boolean addProductTagPairing(ProductTagPairing productTagPairing);
    public boolean addProductHistory(ProductHistory productHistory);
}
