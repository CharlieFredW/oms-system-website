package PIM.domain.dataHandlers;
import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.ProductTagPairing;
import PIM.domain.Tag;

import java.sql.PreparedStatement;
import java.util.List;

public interface DataReaderInterface {
    public List<Product> searchProductsByName(String name, int distance);
    public List<Product> searchProductsByTag(String tag);
    public Product findProductByID(String UUID);
    public List<Product> findAllProducts();
    public Tag findTagByID(String id);
    public Tag findTagByName(String name);
    public List<Tag> findAllTags();
    public List<ProductTagPairing> findProductTagPairingsByProduct(String product_id);
    public List<ProductTagPairing> findProductTagPairingsByTag(String tag);
    public List<ProductTagPairing> returnProductTagPairings(String product_id, PreparedStatement queryStatement);
    public List<ProductTagPairing> findAllProductTagPairings();
    public ProductHistory findProductHistoryByHistoryID(String UUID);
    public ProductHistory findProductHistoryByProduct(Product product);
}
