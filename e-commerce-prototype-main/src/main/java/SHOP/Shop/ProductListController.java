package SHOP.Shop;

import SHOP.Logic.CMSHandler;
import SHOP.Logic.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductListController {


    public void updateProductList(ArrayList<Product> products) {

        MainController.getInstance().getProductPane().getChildren().clear();
        /*for(Product product: MainController.getInstance().getProducts()){
            try{
                CMSHandler cmsHandler = new CMSHandler();
                FXMLLoader fxmlLoader = cmsHandler.getPage("gen_product");
                VBox productBox = fxmlLoader.load();
                ProductController productController = fxmlLoader.getController();
                productController.setProductBoxData(product);
                MainController.getInstance().getProductPane().getChildren().add(productBox);

            } catch (IOException e){
                throw new RuntimeException(e);
            }

        }
        */
        List<Product> allProducts = products;
        int batchSize = 10;
        int totalProducts = allProducts.size();
        CMSHandler cmsHandler = new CMSHandler();

        for (int startIndex = 0; startIndex < totalProducts; startIndex += batchSize) {
            int endIndex = Math.min(startIndex + batchSize, totalProducts);
            List<Product> batchProducts = allProducts.subList(startIndex, endIndex);
            List<VBox> batchProductBoxes = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            // Process the batch of products
            for (Product product : batchProducts) {
                try{
                    FXMLLoader fxmlLoader = cmsHandler.getPage("gen_product");
                    VBox productBox = fxmlLoader.load();
                    ProductController productController = fxmlLoader.getController();
                    //MainController.getInstance().getProductPane().getChildren().add(productBox);
                    productController.setProductBoxData(product);
                    batchProductBoxes.add(productBox);

                } catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
            MainController.getInstance().getProductPane().getChildren().addAll(batchProductBoxes);
            batchProductBoxes.clear();
            long endTime = System.currentTimeMillis();
            //System.out.println("Processed " + batchSize + " products in " + (endTime - startTime) + " ms");
        }
        MainController.getInstance().getScrollPane().setVvalue(0);
    }
}