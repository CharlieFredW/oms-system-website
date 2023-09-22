package CMS.Logic.component;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class roundpane extends component {
    private ArrayList<component> content;

    public roundpane(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color)  {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 5;
    }

    public void addContent(component content)   {
        this.content.add(content);
    }

    public void removeContent(int index)    {
        this.content.remove(this.content.get(index));
    }

    public ArrayList<component> getContent()    {
        return content;
    }

    @Override
    public Node compConvert() {
        javafx.scene.layout.Pane pane = new javafx.scene.layout.Pane();
        pane.setAccessibleText(this.name);
        pane.setLayoutX(this.positionX);
        pane.setLayoutY(this.positionY);
        pane.setPrefWidth(this.sizeW);
        pane.setPrefHeight(this.sizeH);
        pane.setStyle("-fx-background-color: " + colorTrim(this.color) + "; -fx-background-radius: " + this.sizeW);
        this.setReference(pane);
        return pane;
    }
    @Override
    public String exportConvert() {
        String exportString = "<Pane fx:id=\"" + fxid + "\" prefHeight=\"" + sizeH + "\" prefWidth=\"" + sizeW + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" style=\"-fx-background-color: " + colorTrim(color) + "; -fx-background-radius: " + this.sizeW + ";\" />";
        return exportString;
    }
}
