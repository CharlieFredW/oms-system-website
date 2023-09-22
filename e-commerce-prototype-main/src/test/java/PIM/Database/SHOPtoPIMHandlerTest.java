package PIM.Database;

import PIM.domain.SHOPtoPIMHandler;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SHOPtoPIMHandlerTest {
    @Test
    public void getProductsReturnsSomething() {
        assertNotNull(SHOPtoPIMHandler.getInstance().getAllProducts());
        assertFalse(SHOPtoPIMHandler.getInstance().getAllProducts().toString().isEmpty());
    }
    @Test
    public void getProductByNameReturnsSomething() {
        assertFalse(SHOPtoPIMHandler.getInstance().getProductsByName("Iphone 14").toString().trim().replaceAll("\\{\"product\":\\[\\]\\}", "").isEmpty());
        assertTrue(SHOPtoPIMHandler.getInstance().getProductsByName("HUHE").toString().trim().replaceAll("\\{\"product\":\\[\\]\\}", "").isEmpty());
    }
    @Test
    public void getProductByTagsReturnSomething() {
        assertFalse(SHOPtoPIMHandler.getInstance().getProductsByTags("Iphone").toString().trim().replaceAll("\\{\"product\":\\[\\]\\}", "").isEmpty());
        assertNull(SHOPtoPIMHandler.getInstance().getProductsByTags("HUHEWindmill"));
    }
}
