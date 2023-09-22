package CMS.Logic.component;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class image extends component implements ContainsLink{
    private String path;

    public image (String name, String path) {
        super(name);
        this.path = path;
    }

    public image(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color, String path)  {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 6;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Node compConvert() {
        Image image = new Image(path);
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
        imageView.setAccessibleText(this.name);
        imageView.setLayoutX(this.positionX);
        imageView.setLayoutY(this.positionY);
        imageView.setFitWidth(this.sizeW);
        imageView.setFitHeight(this.sizeH);
        this.setReference(imageView);

        return imageView;
    }

    @Override
    public String exportConvert() {
        String exportString = "<ImageView fx:id=\"" + fxid + "\" fitHeight=\"" + sizeH + "\" fitWidth=\"" + sizeW + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" pickOnBounds=\"true\" preserveRatio=\"true\">\n" +
                "    <image>\n" +
                "       <Image url=\"" + this.path + "\" />\n" +
                "    </image>\n" +
                "</ImageView>";
        return exportString;
    }

    @Override
    public void editLink(String newContent) {
        this.path = newContent;
    }

    @Override
    public String getLinkContent() {
        return path;
    }

}
