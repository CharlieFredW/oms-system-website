package SHOP.Logic;

import SHOP.Shop.MainController;
import java.util.ArrayList;


public class Search {

    private ProductHandler productHandler = new ProductHandler();

    public ArrayList<Product> getPhones(){
        ArrayList<Product> phones = new ArrayList<>();
        for(Product product : productHandler.getProductList(true)){
            if(product.getCategory().equalsIgnoreCase("Phone")){
                phones.add(product);
            }
        }
        return phones;
    }
    public ArrayList<Product> getMouse(){
        ArrayList<Product> mouses = new ArrayList<>();
        for(Product product : productHandler.getProductList(true)){
            if(product.getCategory().equalsIgnoreCase("Mouse")){
                mouses.add(product);
            }
        }
        return mouses;
    }
    public ArrayList<Product> getCPU(){
        ArrayList<Product> cpus = new ArrayList<>();
        for(Product product : productHandler.getProductList(true)){
            if(product.getCategory().equalsIgnoreCase("CPU")){
                cpus.add(product);
            }
        }
        return cpus;
    }
    public ArrayList<Product> getSearchResults(String input){
        ArrayList<Product> results = new ArrayList<>();
        for (Product product: productHandler.getProductList(true)){
            if(product.getTitle().equalsIgnoreCase(input)){
                results.add(product);
            }
            if(product.getCategory().equalsIgnoreCase(input)){
                results.add(product);
            }
        }
        return results;
    }

    public void search(){
        MainController mainController = MainController.getInstance();
        if (!mainController.getSearchBar().getText().isEmpty()) {
            mainController.getBorderPane().setLeft(mainController.getLeftBarBox());
            mainController.getScrollPane().setContent(mainController.getProductPane());
            ArrayList<Product> list = getSearchResults(mainController.getSearchBar().getText());
            mainController.getProductListController().updateProductList(list);
            mainController.getSearchBar().setText("");
        }
    }
}
