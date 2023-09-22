package CMS.Logic.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class textblock extends component implements ContainsText {

    public textblock(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color, String textContent) {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 4;
        this.textContent = textContent;
    }

    @Override
    public Node compConvert() {
        javafx.scene.control.Label label = new javafx.scene.control.Label();

        label.setAccessibleText(this.name);
        label.setText(this.textContent);
        label.setLayoutX(this.positionX);
        label.setLayoutY(this.positionY);
        label.setFont(new Font(this.sizeH));
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill:" + colorTrim(this.color) + ";");
        this.setReference(label);
        return label;
    }

    @Override
    public String exportConvert() {
        String exportString = "<Label fx:id=\"" + fxid + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" text=\"" + this.textContent + "\" textFill=\""+ colorTrim(this.color) +"\"><font>\n" +
                "                  <Font size=\"" + sizeH + "\" />\n" +
                "               </font></Label>";
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
