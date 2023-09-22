package SHOP.Shop;

public class PHPageController implements Controller {

    private PHCategoryController categoryPageController;

    public void showProductHelper() {
        MainController.getInstance().getBorderPane().setLeft(null);
        categoryPageController = (PHCategoryController) MainController.getInstance().setMainWindow("PHCategorySelection");
        categoryPageController.fillCategories();
    }
}