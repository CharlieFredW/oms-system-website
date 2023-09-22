package PIM.domain;

import java.util.List;

public interface IPersistenceHandler {
    Product findProductByID(String UUID);

    List<Product> findAllProducts();

    boolean deleteProduct(Product product);

    boolean addProduct(Product product);

    boolean addTag(Tag tag);

    boolean deleteTag(String id);

    List<Tag> findAllTags();

    Tag findTagByID(String id);

    boolean addProductTagPairing(ProductTagPairing productTagPairing);

    boolean deleteProductTagPairingByProduct(Product product);

    boolean addProductHistory(ProductHistory productHistory);

    ProductHistory findProductHistoryByHistoryID(String UUID);

    ProductHistory findProductHistoryByProduct(Product product);

    boolean deleteProductHistory(ProductHistory productHistory);

    List<ProductTagPairing> findAllProductTagPairings();

    List<ProductTagPairing> findProductTagPairingsByProduct(String product_id);

    List<ProductTagPairing> findProductTagPairingsByTag(String id);

    List<Product> searchProductsByName(String searchText, int maxLevenshteinDistance);

    List<Product> searchProductsByTag(String searchText);

    boolean deleteProductTagPairingByTag(Tag tag);
}
