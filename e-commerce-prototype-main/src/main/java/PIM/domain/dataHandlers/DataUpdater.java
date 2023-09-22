package PIM.domain.dataHandlers;

import PIM.domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataUpdater implements DataUpdaterInterface {
    private Connection databaseConnection;
    public DataUpdater(Connection connection) {
        this.databaseConnection = connection;
    }
    @Override
    public boolean updateProduct(Product product){
        try {
            PreparedStatement alterStatement = databaseConnection.prepareStatement(
                    "UPDATE product SET name=?, description=?, color=?, stock=?, category=?, price=?, discount=? WHERE product_id=?");
            alterStatement.setString(1, product.getName());
            alterStatement.setString(2, product.getDescription());
            alterStatement.setString(3, product.getColor());
            alterStatement.setInt(4, product.getStock());
            alterStatement.setString(5, product.getCategory());
            alterStatement.setDouble(6, product.getPrice());
            alterStatement.setDouble(7, product.getDiscount());
            alterStatement.setString(8, product.getProduct_id());

            int rowsAffected = alterStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
