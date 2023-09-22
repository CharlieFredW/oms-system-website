package PIM.domain.dataHandlers;

import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.ProductTagPairing;
import PIM.domain.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataCreater implements DataCreaterInterface {
    private Connection databaseConnection;
    public DataCreater(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
    @Override
    public boolean addProduct(Product product){
        try {
            addProductHistory(product.getProductHistory());
            System.out.println(databaseConnection.isValid(3));
            PreparedStatement insertStatement = databaseConnection.prepareStatement(
                    "INSERT INTO product(product_id,name,description,color,stock,category,history_id,price,discount) VALUES (?,?,?,?,?,?,?,?,?)");
            insertStatement.setString(1,String.valueOf(product.getProduct_id()));
            insertStatement.setString(2,product.getName());
            insertStatement.setString(3,product.getDescription());
            insertStatement.setString(4,product.getColor());
            insertStatement.setInt(   5,product.getStock());
            insertStatement.setString(6,product.getCategory());
            insertStatement.setString(7,product.getProductHistory().getProduct_history_id());
            insertStatement.setDouble(8,product.getPrice());
            insertStatement.setDouble(9,product.getDiscount());
            return insertStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean addTag(Tag tag) {
        try {
            PreparedStatement insertStatement = databaseConnection.prepareStatement("INSERT INTO tag(tag_id, name) VALUES (?, ?)");
            insertStatement.setString(1, tag.getTag_id());
            insertStatement.setString(2, tag.getName());
            return insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean addProductTagPairing(ProductTagPairing productTagPairing) {
        try {
            PreparedStatement insertStatement = databaseConnection.prepareStatement(
                    "INSERT INTO product_tag(product_id, tag_id) VALUES (?,?)");
            insertStatement.setString(1, productTagPairing.getProduct_id());
            insertStatement.setString(2, productTagPairing.getTag_id());
            return insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean addProductHistory(ProductHistory productHistory) {
        try {
            PreparedStatement insertStatement = databaseConnection.prepareStatement(
                    "INSERT INTO product_history(history_id, last_sale, created) VALUES (?,?,?)");
            insertStatement.setString(1, productHistory.getProduct_history_id());
            insertStatement.setDate(2, productHistory.getLastSale());
            insertStatement.setDate(3, productHistory.getCreatedOn());
            return insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
