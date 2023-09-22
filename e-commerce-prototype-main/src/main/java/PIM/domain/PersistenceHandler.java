package PIM.domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import PIM.domain.dataHandlers.*;
import javafx.scene.chart.PieChart;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class PersistenceHandler implements IPersistenceHandler {

    private static PersistenceHandler instance;
    private String url = "db.xdclgcxmcwqjwpqjtfxp.supabase.co/postgres";
    private int port = 5432;
    private String databaseName = "postgres";
    private String username = "postgres";
    private String password = "Ot0PnSOnkY7I3CIP";
    private Connection connection = null;
    private DataReader datareader;
    private DataUpdater dataupdater;
    private DataDeleter datadeleter;
    private DataCreater datacreater;

    private PersistenceHandler() {
        initializePostgresqlDatabase();
        this.datareader = new DataReader(connection);
        this.dataupdater = new DataUpdater(connection);
        this.datadeleter = new DataDeleter(connection);
        this.datacreater = new DataCreater(connection);
    }

    /* Using Singleton pattern to make sure there is only 1 instance of the connection being used */
    public static PersistenceHandler getInstance() {
        if (instance == null) {
            instance = new PersistenceHandler();
        }
        return instance;
    }

    private void initializePostgresqlDatabase() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://db.xdclgcxmcwqjwpqjtfxp.supabase.co:5432/postgres?user=postgres&password=Ot0PnSOnkY7I3CIP");
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) System.exit(-1);
        }

    }



    @Override
    public Product findProductByID(String UUID) {
        return datareader.findProductByID(UUID);
    }
    @Override
    public List<Product> findAllProducts() {
        return datareader.findAllProducts();
    }

    @Override
    public boolean deleteProduct(Product product) {
        return datadeleter.deleteProduct(product);
    }

    @Override
    public boolean addProduct(Product product){
        return datacreater.addProduct(product);
    }

    @Override
    public boolean addTag(Tag tag) {
        return datacreater.addTag(tag);
    }

    @Override
    public boolean deleteTag(String id) {
       return datadeleter.deleteTag(id);
    }

    @Override
    public List<Tag> findAllTags() {
        return datareader.findAllTags();
    }
    @Override
    public Tag findTagByID(String id) {
        return datareader.findTagByID(id);
    }
    public Tag findTagByName(String name) {
        return datareader.findTagByName(name);
    }
    @Override
    public boolean addProductTagPairing(ProductTagPairing productTagPairing) {
        return datacreater.addProductTagPairing(productTagPairing);
    }

    @Override
    public boolean deleteProductTagPairingByProduct(Product product) {
        return datadeleter.deleteProductTagPairingByProduct(product);
    }

    @Override
    public boolean addProductHistory(ProductHistory productHistory) {
        return addProductHistory(productHistory);
    }

    @Override
    public ProductHistory findProductHistoryByHistoryID(String UUID) {
       return findProductHistoryByHistoryID(UUID);
    }

    @Override
    public ProductHistory findProductHistoryByProduct(Product product) {
        return datareader.findProductHistoryByProduct(product);
    }

    @Override
    public boolean deleteProductHistory(ProductHistory productHistory) {
        return datadeleter.deleteProductHistory(productHistory);
    }

    @Override
    public List<ProductTagPairing> findAllProductTagPairings() {
        return datareader.findAllProductTagPairings();
    }

    @Override
    public List<ProductTagPairing> findProductTagPairingsByProduct(String product_id) {
        return datareader.findProductTagPairingsByProduct(product_id);
    }


    private List<ProductTagPairing> returnProductTagPairings(String product_id, PreparedStatement queryStatement) throws SQLException {
        return datareader.returnProductTagPairings(product_id, queryStatement);
    }

    @Override
    public List<ProductTagPairing> findProductTagPairingsByTag(String tag_id) {
        return findProductTagPairingsByTag(tag_id);
    }
    @Override
    public List<Product> searchProductsByName(String searchText, int maxLevenshteinDistance) {
        return datareader.searchProductsByName(searchText, maxLevenshteinDistance);
    }

    @Override
    public List<Product> searchProductsByTag(String searchText) {
        return datareader.searchProductsByTag(searchText);
    }

    @Override
    public boolean deleteProductTagPairingByTag(Tag tag) {
        return false;
    }

    public boolean updateProduct(Product product){
       return dataupdater.updateProduct(product);
    }

    public Connection getConnection() {
        return connection;
    }

}
