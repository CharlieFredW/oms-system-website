package PIM.domain.dataHandlers;

import PIM.domain.Product;
import PIM.domain.ProductHistory;
import PIM.domain.ProductTagPairing;
import PIM.domain.Tag;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataReader implements DataReaderInterface {
    private Connection databaseConnection;
    public DataReader(Connection connection) {
        this.databaseConnection = connection;
    }

    @Override
    public List<Product> searchProductsByName(String searchText, int maxLevenshteinDistance) {
        // searchText.trim().replaceAll("[ ]","");
        List<Product> products = new ArrayList<>();
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        System.out.println(searchText);
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT product_id, name, price, color, stock FROM product");
            ResultSet resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String id = resultSet.getString("product_id");
                //String description = resultSet.getString("description");
                String color = resultSet.getString("color");
                int stock = resultSet.getInt("stock");
                //String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                //double discount = resultSet.getDouble("discount");
                if (levenshteinDistance.apply(searchText,name) <= maxLevenshteinDistance) {
                    products.add(new Product(id, name, color, price, stock));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public List<Product> searchProductsByTag(String searchText) {
        searchText = searchText.toLowerCase();
        List<Product> products = new ArrayList<>();
        List<ProductTagPairing> productTagPairings;

        System.out.println(searchText);
        Tag foundTag = findTagByName(searchText);
        try {
            System.out.println("A tag was found with the name " + foundTag.getName() + " and id "
                    + foundTag.getTag_id());

            // Find all product-tag-pairing for the tag by searching for the tag_id
            productTagPairings = findProductTagPairingsByTag(foundTag.getTag_id());

            // Getting the product information for every found product by inputting the product_id from
            // each product-tag-pairing into the getProduct method.
            for (ProductTagPairing productTagPairing : productTagPairings) {
                products.add(findProductByID(productTagPairing.getProduct_id()));
            }
            for (Product prod : products) {
                System.out.println(prod.getName());
            }
            return products;
        } catch(NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public Product findProductByID(String UUID) {

        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM product WHERE product_id = ?");
            queryStatement.setString(1, String.valueOf(UUID));
            ResultSet sqlReturnValues = queryStatement.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            return new Product(
                    sqlReturnValues.getString("product_id"),
                    sqlReturnValues.getString("name"),
                    sqlReturnValues.getString("description"),
                    sqlReturnValues.getString("color"),
                    sqlReturnValues.getInt("stock"),
                    sqlReturnValues.getString("category"),
                    sqlReturnValues.getDouble("price"),
                    sqlReturnValues.getDouble("discount")
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAllProducts() {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT product_id, name, color, price, stock, description, category FROM product");
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            List<Product> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                returnValue.add(new Product(
                        sqlReturnValues.getString("product_id"),
                        sqlReturnValues.getString("name"),
                        sqlReturnValues.getString("color"),
                        sqlReturnValues.getDouble("price"),
                        sqlReturnValues.getInt("stock"),
                        sqlReturnValues.getString("description"),
                        sqlReturnValues.getString("category")
                ));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /*
    @Override
    public List<Product> findAllProducts() {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM product");
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            List<Product> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                returnValue.add(new Product(
                        sqlReturnValues.getString("product_id"),
                        sqlReturnValues.getString("name"),
                        sqlReturnValues.getString("description"),
                        sqlReturnValues.getString("color"),
                        sqlReturnValues.getInt("stock"),
                        sqlReturnValues.getString("category"),
                        sqlReturnValues.getDouble("price"),
                        sqlReturnValues.getDouble("discount")
                ));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

     */
    @Override
    public Tag findTagByID(String id) {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM tag WHERE tag_id = ?");
            queryStatement.setString(1, id);
            ResultSet sqlReturnValues = queryStatement.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            return new Tag(sqlReturnValues.getString("tag_id"),
                    sqlReturnValues.getString("name"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Tag findTagByName(String name) {
        name = name.toLowerCase().trim();
        try {
            PreparedStatement queryTags = databaseConnection.prepareStatement(
                    "SELECT * FROM tag WHERE name = ?");
            queryTags.setString(1, name);
            ResultSet sqlReturnValues = queryTags.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            Tag foundTag = new Tag(sqlReturnValues.getString(1),
                    sqlReturnValues.getString(2));
            System.out.println("found the following tag: " + foundTag.getName() + " with id: "+ foundTag.getTag_id());
            return foundTag;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException ne) {
            throw new NullPointerException("No tag found by that name!");
        }
    }
    @Override
    public List<Tag> findAllTags() {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM tag");
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            List<Tag> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                returnValue.add(new Tag(
                        sqlReturnValues.getString(1),
                        sqlReturnValues.getString(2)

                ));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ProductTagPairing> findProductTagPairingsByProduct(String product_id) {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement(
                    "SELECT * FROM product_tag WHERE product_id = ?");
            return returnProductTagPairings(product_id, queryStatement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ProductTagPairing> findProductTagPairingsByTag(String tag_id) {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement(
                    "SELECT * FROM product_tag WHERE tag_id = ?");
            return returnProductTagPairings(tag_id, queryStatement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ProductTagPairing> returnProductTagPairings(String product_id, PreparedStatement queryStatement) {
        try {
            queryStatement.setString(1, product_id);
            ResultSet sqlReturnValues = queryStatement.executeQuery();
            List<ProductTagPairing> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                returnValue.add(new ProductTagPairing(
                        sqlReturnValues.getString("product_id"),
                        sqlReturnValues.getString("tag_id")
                ));
            }
            return returnValue;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ProductTagPairing> findAllProductTagPairings() {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM product_tag");
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            List<ProductTagPairing> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                returnValue.add(new ProductTagPairing(
                        sqlReturnValues.getString("product_id"),
                        sqlReturnValues.getString("tag_id")
                ));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public ProductHistory findProductHistoryByHistoryID(String UUID) {
        try {
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM product_history WHERE history_id = ?");
            queryStatement.setString(1, UUID);
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            PreparedStatement queryProduct = databaseConnection.prepareStatement("SELECT product_id FROM product WHERE history_id = ?");
            queryProduct.setString(1, sqlReturnValues.getString("history_id"));
            ResultSet sqlProductReturn = queryProduct.executeQuery();

            if (!sqlReturnValues.next()) {
                return null;
            }
            return new ProductHistory(
                    sqlReturnValues.getString("history_id"),
                    sqlReturnValues.getDate("last_sale"),
                    sqlReturnValues.getDate("created"),
                    sqlProductReturn.getString("product_id")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ProductHistory findProductHistoryByProduct(Product product) {
        try {
            PreparedStatement query1 = databaseConnection.prepareStatement("SELECT history_id FROM product WHERE product_id = ?");
            query1.setString(1, product.getProduct_id());
            ResultSet sqlReturn1 = query1.executeQuery();
            if (!sqlReturn1.next()) {
                return null;
            }
            String historyID = sqlReturn1.getString("history_id");
            PreparedStatement queryStatement = databaseConnection.prepareStatement("SELECT * FROM product_history WHERE history_id = ?");
            queryStatement.setString(1, historyID);
            ResultSet sqlReturnValues = queryStatement.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            return new ProductHistory(
                    sqlReturnValues.getString("history_id"),
                    sqlReturnValues.getDate("last_sale"),
                    sqlReturnValues.getDate("created"),
                    product.getProduct_id()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
