package OMS.UI.OtherGroupsAPIS;

public interface IUpdateStock {
    //method for pim to update stock based on orders

    boolean updateStock(int productId, int quantity);
}
