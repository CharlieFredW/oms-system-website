package CMS.componentTests;

import CMS.Logic.component.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

public class componentParameterTests {

    @Test
    public void testComponentParameters() {
        component testComponent = new image("test", "", 0, 0, 0, 0, Color.BLACK, "test");
        assert(testComponent.getName().equals("test"));
        assert(testComponent.getPositionX() == 0);
        assert(testComponent.getPositionY() == 0);
        assert(testComponent.getSizeW() == 0);
        assert(testComponent.getSizeH() == 0);
        assert(testComponent.colorTrim(testComponent.getColor()).equals("#000000"));
        assert(testComponent.getTypeID() == 6);
        assert !(testComponent instanceof ContainsLink) || (((ContainsLink) testComponent).getLinkContent().equals("test"));
    }

    @Test
    public void testChangesAreSavedOnComponent() {
        component testComponent = new image("test", "", 0, 0, 0, 0, Color.BLACK, "test");
        testComponent.setName("test2");
        testComponent.setPositionX(1);
        testComponent.setPositionY(1);
        testComponent.setSizeW(1);
        testComponent.setSizeH(1);
        testComponent.setColor(Color.WHITE);
        ((ContainsLink) testComponent).editLink("test2");
        assert(testComponent.getName().equals("test2"));
        assert(testComponent.getPositionX() == 1);
        assert(testComponent.getPositionY() == 1);
        assert(testComponent.getSizeW() == 1);
        assert(testComponent.getSizeH() == 1);
        assert(testComponent.colorTrim(testComponent.getColor()).equals("#ffffff"));
        assert(testComponent.getTypeID() == 6);
        assert !(testComponent instanceof ContainsLink) || (((ContainsLink) testComponent).getLinkContent().equals("test2"));
    }

    @Test
    public void testComponentOnlyHasNeededParameters() {
        component testComponent = new pane("test", "", 0, 0, 0, 0, Color.BLACK);
        assert(testComponent.getName().equals("test"));
        assert(testComponent.getPositionX() == 0);
        assert(testComponent.getPositionY() == 0);
        assert(testComponent.getSizeW() == 0);
        assert(testComponent.getSizeH() == 0);
        assert(testComponent.colorTrim(testComponent.getColor()).equals("#000000"));
        assert(testComponent.getTypeID() == 3);
        assert !(testComponent instanceof ContainsText);
        assert !(testComponent instanceof ContainsLink);
    }

    @Test
    public void testComponentCreatesReference() {
        component testComponent = new pane("test", "", 0, 0, 0, 0, Color.BLACK);
        //Reference is only created when the component is converted to fxml.
        testComponent.compConvert();
        assert(testComponent.getReference() != null);
        assert(testComponent.getReference() instanceof javafx.scene.layout.Pane);
    }

    @Test
    public void testComponentWithinXYBounds() {
        component testComponent = new pane("test", "", 0, 0, 0, 0, Color.BLACK);
    }
}
