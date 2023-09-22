package CMS.Logic.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class textfield extends component implements ContainsText{
    public textfield(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color, String textContent) {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 7;
        this.textContent = textContent;
    }

    @Override
    public Node compConvert() {
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();

        textField.setAccessibleText(this.name);
        textField.setText(this.textContent);
        textField.setLayoutX(this.positionX);
        textField.setLayoutY(this.positionY);
        textField.setFont(new Font(this.sizeH));
        textField.setAlignment(Pos.CENTER);
        textField.setStyle("-fx-text-fill:" + colorTrim(this.color) + ";");
        this.setReference(textField);
        return textField;
    }

    @Override
    public String exportConvert() {
        String exportString = "<TextField fx:id=\"" + fxid + "\" prefHeight=\"" + sizeH + "\" prefWidth=\"" + sizeW + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" text=\"" + this.textContent + "\" />";
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
