package PIM.domain.dataHandlers;

import PIM.domain.Product;

import java.sql.Connection;

public interface DataUpdaterInterface {
    public boolean updateProduct(Product product);

}
