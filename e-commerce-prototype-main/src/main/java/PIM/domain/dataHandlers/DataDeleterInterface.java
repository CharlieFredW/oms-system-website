package PIM.domain.dataHandlers;

import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.Tag;

public interface DataDeleterInterface {
    public boolean deleteProduct(Product product);
    public boolean deleteTag(String tag);
    public boolean deleteProductTagPairingByProduct(Product product);
    public boolean deleteProductTagPairingByTag(Tag tag);
    public boolean deleteProductHistory(ProductHistory productHistory);
}
