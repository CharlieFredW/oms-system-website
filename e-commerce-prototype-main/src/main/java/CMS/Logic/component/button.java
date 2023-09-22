package CMS.Logic.component;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public class button extends component implements ContainsText {

    public button(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color, String textContent)  {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 2;
        this.textContent = textContent;
    }


    @Override
    public Node compConvert() {
        javafx.scene.control.Button button = new javafx.scene.control.Button();
        button.setAccessibleText(this.name);
        button.setText(this.textContent);
        button.setLayoutX(this.positionX);
        button.setLayoutY(this.positionY);
        button.setPrefWidth(this.sizeW);
        button.setPrefHeight(this.sizeH);
        button.setStyle("-fx-background-color: " + colorTrim(this.color) + ";");
        this.setReference(button);
        return button;
    }

    @Override
    public String exportConvert() {
        String exportString = "<Button fx:id=\"" + fxid + "\" prefHeight=\"" + sizeH + "\" prefWidth=\"" + sizeW + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" mnemonicParsing=\"false\" text=\"" + this.textContent + "\" />";
        return exportString;
    }

    @Override
    public void editText(String newContent) {
        this.textContent = newContent;
    }

    @Override
    public String getTextContent() {
        return textContent;
    }
}
