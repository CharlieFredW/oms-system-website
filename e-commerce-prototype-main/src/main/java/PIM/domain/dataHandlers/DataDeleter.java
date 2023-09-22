package PIM.domain.dataHandlers;

import PIM.domain.PersistenceHandler;
import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataDeleter implements DataDeleterInterface {
    private Connection databaseConnection;
    public DataDeleter(Connection connection) {
        this.databaseConnection = connection;
    }
    @Override
    public boolean deleteProduct(Product product) {
        try {
            ProductHistory productHistory = PersistenceHandler.getInstance().findProductHistoryByProduct(product);
            deleteProductTagPairingByProduct(product);
            PreparedStatement deleteStatement = databaseConnection.prepareStatement("DELETE FROM product WHERE product_id = ?");
            deleteStatement.setString(1, product.getProduct_id());
            boolean deletionSuccess = deleteStatement.execute();
            deleteProductHistory(productHistory);
            return deletionSuccess;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deleteTag(String id) {
        try {
            PreparedStatement deleteStatement = databaseConnection.prepareStatement("DELETE FROM tag WHERE tag_id = ?");
            deleteStatement.setString(1, id);
            return deleteStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deleteProductTagPairingByProduct(Product product) {
        try {
            PreparedStatement deleteStatement = databaseConnection.prepareStatement("DELETE FROM product_tag WHERE product_id = ?");
            deleteStatement.setString(1, product.getProduct_id());
            return deleteStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deleteProductTagPairingByTag(Tag tag) {
        return false;
    }
    @Override
    public boolean deleteProductHistory(ProductHistory productHistory) {
        try {
//            PreparedStatement query1 = connection.prepareStatement("SELECT history_id FROM product WHERE product_id = ?");
//            query1.setString(1, product.getProduct_id());
//            ResultSet sqlReturn1 = query1.executeQuery();
            PreparedStatement deleteStatement = databaseConnection.prepareStatement("DELETE FROM product_history WHERE history_id = ?");
            deleteStatement.setString(1, productHistory.getProduct_history_id());
            return deleteStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
