package CMS.Logic;

import CMS.Logic.component.ContainsLink;
import CMS.Logic.component.ContainsText;
import CMS.Logic.component.component;
import CMS.Logic.component.image;
import CMS.Logic.component.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;

public class SelectableComponent {
    private List<component> componentsList;
    private ListView components_listview;
    private double mouseAnchorX;
    private double mouseAnchorY;
    private final Label textOrLinkLabel;
    private final ChoiceBox<image> imageChoiceBox;
    private final TextField name, fxid, type, textContent, width, height, x, y;
    private final ColorPicker color;
    private final Button create, apply, delete;
    private Node nodeSelection;
    private component compSelection;
    private static Node lastSelection;
    private static String lastSelectionStyle;

    SelectableComponent(List<component> componentsList, ListView<component> components_listview, Label textOrLinkLabel, ChoiceBox<image> imageChoiceBox, TextField name, TextField fxid, TextField type, ColorPicker color, TextField textContent, TextField width, TextField height, TextField x, TextField y, Button create, Button apply, Button delete){
        this.componentsList = componentsList;
        this.components_listview = components_listview;
        this.textOrLinkLabel = textOrLinkLabel;
        this.imageChoiceBox = imageChoiceBox;
        this.name = name;
        this.fxid = fxid;
        this.type = type;
        this.color = color;
        this.textContent = textContent;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.create = create;
        this.apply = apply;
        this.delete = delete;
    }
    public void makeSelectable(Node node, Boolean isMenuComponent) {
        if(node.getId() == "preview_window"){
            System.out.println("Deselect!");
        }
        if(isMenuComponent) {  // menu component
            node.setOnMousePressed(mouseEvent -> {
                components_listview.getSelectionModel().clearSelection();

                lastSelectionHandler(node);
                nodeSelection = node;

                // Update properties information upon component selection from menu window
                resetProperties();
                type.setText(nodeSelection.getId().substring(10).toUpperCase());
                x.setText("0");
                y.setText("0");

                // Update corresponding buttons
                if(node.getAccessibleText() != null){   // enabling/disabling text field from menu components
                    textOrLinkLabel.setMaxHeight(Region.USE_COMPUTED_SIZE);
                    textOrLinkLabel.setVisible(true);
                    textContent.setMaxHeight(Region.USE_COMPUTED_SIZE);
                    textContent.setVisible(true);
                    textContent.setDisable(false);
                    if(node.getAccessibleText().equals("textContent")){ // for menu items with text content
                        textOrLinkLabel.setText("Text:");
                        textContent.setVisible(true);
                        textContent.setDisable(false);
                    }
                    if(node.getAccessibleText().equals("linkContent")){ // for menu items with link content
                        textOrLinkLabel.setText("Link:");
                        textContent.setVisible(true);
                        textContent.setDisable(false);
                    }
                    if(node.getAccessibleText().equals("imageContent")){ // for menu items with link content
                        textOrLinkLabel.setText("Image:");
                        imageChoiceBox.setVisible(true);
                        imageChoiceBox.setDisable(false);
                    }
                } else {
                    textOrLinkLabel.setMaxHeight(0);
                    textOrLinkLabel.setVisible(false);
                    textContent.setMaxHeight(0);
                    textContent.setVisible(false);
                    textContent.setDisable(true);
                    imageChoiceBox.setMaxHeight(0);
                    imageChoiceBox.setVisible(false);
                    imageChoiceBox.setDisable(true);
                }
                create.setDisable(false);
                apply.setDisable(true);
                delete.setDisable(true);
                lastSelection = node;
            });
        } else { /** preview_window component **/
            double startX = node.getLayoutX();
            double startY = node.getLayoutY();

            node.setOnMousePressed(mouseEvent -> {
                mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
                mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();

                updateComponentSelection(node);
            });

            node.setOnMouseDragged(mouseEvent -> {
                AnchorPane anchorPane = (AnchorPane) node.getParent();
                if((0 < (mouseEvent.getSceneX() - mouseAnchorX + startX)) && (mouseEvent.getSceneX() - mouseAnchorX + startX + node.getBoundsInParent().getWidth()) < anchorPane.getWidth()){
                    node.setTranslateX(mouseEvent.getSceneX() - mouseAnchorX);
                    x.setText(String.valueOf(node.getBoundsInParent().getCenterX() - node.getBoundsInParent().getWidth()/2));
                }
                if((0 < (mouseEvent.getSceneY() - mouseAnchorY + startY)) && (mouseEvent.getSceneY() - mouseAnchorY + startY + node.getBoundsInParent().getHeight()) < anchorPane.getHeight()){
                    node.setTranslateY(mouseEvent.getSceneY() - mouseAnchorY);
                    y.setText(String.valueOf(node.getBoundsInParent().getCenterY() - node.getBoundsInParent().getHeight()/2));
                }
            });

            node.setOnMouseReleased(mouseEvent -> {
                AnchorPane anchorPane = (AnchorPane) node.getParent();

                double newX = (node.getBoundsInParent().getCenterX() - node.getBoundsInParent().getWidth()/2);
                double newY = (node.getBoundsInParent().getCenterY() - node.getBoundsInParent().getHeight()/2);

                // Update node info to object
                compSelection.setPositionX(newX);
                compSelection.setPositionY(newY);
            });
        }
    }
    public void updateComponentSelection(Node node) {
        for (component comp : componentsList){
            if(comp.getName() == node.getAccessibleText()){
                compSelection = comp;
            }
        }

        components_listview.getSelectionModel().select(compSelection);
        components_listview.scrollTo(compSelection);

        lastSelectionHandler(node);

        System.out.println(compSelection.getTypeID() + " selected! ID=" + compSelection.getName());

        // Update properties information upon component selection from preview window
        updateProperties(compSelection);

        // Update corresponding buttons
        create.setDisable(true);
        apply.setDisable(false);
        delete.setDisable(false);
        lastSelection = node;
    }
    private void lastSelectionHandler(Node node){
        if (lastSelection != null) {    // if last selection exist, reset selection style
            lastSelection.setStyle(lastSelectionStyle);
        }
        lastSelectionStyle = node.getStyle();
        node.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-border-radius: 1px;" + lastSelectionStyle);
    }
    private void updateProperties(component comp){
        name.setText(comp.getName());
        fxid.setText(comp.getFxid());
        type.setText(comp.idToString());
        color.setValue(compSelection.getColor());
        if(comp instanceof ContainsText){      // Enabling and disabling text field from content components
            textOrLinkLabel.setText("Text:");
            textOrLinkLabel.setVisible(true);
            textContent.setVisible(true);
            textContent.setDisable(false);
            ContainsText c = (ContainsText) comp;
            textContent.setText(c.getTextContent());
        } else if(comp instanceof ContainsLink) {
            textOrLinkLabel.setText("Link:");
            textOrLinkLabel.setVisible(true);
            textContent.setVisible(true);
            textContent.setDisable(false);
            ContainsLink c = (ContainsLink) comp;
            textContent.setText(c.getLinkContent());
        } else {
            textOrLinkLabel.setVisible(false);
            textContent.setVisible(false);
            textContent.setDisable(true);
            textContent.setText("");
        }
        width.setText(String.valueOf(comp.getSizeW()));
        height.setText(String.valueOf(comp.getSizeH()));
        x.setText(String.valueOf(comp.getPositionX()));
        y.setText(String.valueOf(comp.getPositionY()));
    }
    public void deselectComponent(){
        if (lastSelection != null) {    // if last selection exist, reset selection style and clear the component selection
            lastSelection.setStyle(lastSelectionStyle);
            lastSelection = null;
            apply.setDisable(true);
            delete.setDisable(true);
            components_listview.getSelectionModel().clearSelection();
        }
    }
    public void resetProperties(){
        name.setText("");
        fxid.setText("");
        type.setText("");
        color.setValue(new Color(1, 1,1,1));
        textContent.setText("");
        width.setText("");
        height.setText("");
        x.setText("");
        y.setText("");
    }
    public component getSelection(){
        return compSelection;
    }
}
