package SHOP.Shop;

import SHOP.Logic.OMSHandler;
import SHOP.Logic.Order;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {
    OMSHandler omsHandler;
    @BeforeEach
    void setUp() {
        omsHandler = new OMSHandler();
    }

    @Test
    void checkShippingInputs() {
        assertTrue(Pattern.matches("^[0-9]{8}$","12345678"));
        assertTrue(Pattern.matches("^\\d+$","1233"));
        assertTrue(Pattern.matches("^\\w+$","Amalie"));
        assertTrue(Pattern.matches("^(?=.*\\w)(?=.*@).*$","amalie@mail"));

        assertFalse(Pattern.matches("^[0-9]{8}$","1234568"));
        assertFalse(Pattern.matches("^[0-9]{8}$","123456789"));
        assertFalse(Pattern.matches("^[0-9]{8}$","a"));
        assertFalse(Pattern.matches("^(?=.*\\w)(?=.*@).*$","amaliemail"));
    }

    @Test
    void checkBillingInputs() {
        assertTrue(Pattern.matches("^[0-9]{3}$","123"));
        assertTrue(Pattern.matches("^[0-9]{16}$","1234567891234567"));
        assertTrue(Pattern.matches("^[0-9]{2}$","22"));

        assertFalse(Pattern.matches("^[0-9]{3}$","1231"));
        assertFalse(Pattern.matches("^[0-9]{16}$","12345678912345671"));
        assertFalse(Pattern.matches("^[0-9]{2}$","221"));

        assertFalse(Pattern.matches("^[0-9]{3}$","12"));
        assertFalse(Pattern.matches("^[0-9]{16}$","12345678912"));
        assertFalse(Pattern.matches("^[0-9]{2}$","1"));
    }
}