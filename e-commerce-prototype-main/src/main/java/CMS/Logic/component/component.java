package CMS.Logic.component;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class component {
    protected String name;
    protected String fxid;
    protected Node reference;
    protected int ID;
    protected int typeID;
    protected double positionX, positionY;
    protected double sizeW, sizeH;
    protected Color color;
    protected String textContent;

    public component(String name) {
        this.name = name;
    }

    public component(String name, String fxid, double positionX, double positionY, double sizeW, double sizeH, Color color) {
        this.name = name;
        this.fxid = fxid;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeW = sizeW;
        this.sizeH = sizeH;
        this.color = color;
    }

    //Convert the component into a renderable node.
    public abstract Node compConvert();
    //Convert the component into a node that can be exported to a .fxml file.
    public abstract String exportConvert();

    public String colorTrim(Color color){
        return "#" + (color.toString().substring(2, color.toString().length()-2));
    }
    public String idToString() {
        return (this.getClass().getSimpleName()).toUpperCase();
    }
    @Override
    public String toString() { return "(" + this.idToString() + ")\t\t" + this.name; }

    public String getName() {
        return name;
    }
    public Node getReference() { return reference; }

    public int getTypeID() {
        return typeID;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getSizeW() {
        return sizeW;
    }

    public double getSizeH() {
        return sizeH;
    }

    public Color getColor() { return color; }

    public String getFxid() { return fxid; }

    public void setName(String name) { this.name = name; }

    public void setReference(Node reference) { this.reference = reference; }

    public void setID(int ID) { this.ID = ID; }

    public void setTypeID(int typeID) { this.typeID = typeID; }

    public void setPositionX(double positionX) { this.positionX = positionX; }

    public void setPositionY(double positionY) { this.positionY = positionY; }

    public void setSizeW(double sizeW) {
        this.sizeW = sizeW;
    }

    public void setSizeH(double sizeH) {
        this.sizeH = sizeH;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFxid(String fxid) { this.fxid = fxid; }
}
/*
### TypeID's ###

1 - Subscene
2 - Button
3 - pane
4 - Text
5 - roundpane
6 - image
7 - textfield

 */