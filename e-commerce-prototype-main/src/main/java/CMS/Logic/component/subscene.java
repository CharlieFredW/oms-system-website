package CMS.Logic.component;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class subscene extends component {
    ArrayList<component> components_list;

    public subscene(String name, String fxid, double posx, double posy, double sizew, double sizeh, Color color)  {
        super(name, fxid, posx, posy, sizew, sizeh, color);
        this.typeID = 1;
        this.components_list = new ArrayList<>();
    }
    private void addComponent(component comp){
        components_list.add(comp);
    }
    private void removeComponent(component comp) {
        components_list.remove(comp);
    }
    public ArrayList<component> getComponents_list(){
        return components_list;
    }

    @Override
    public Node compConvert() {
        AnchorPane scene = new AnchorPane();
        scene.setAccessibleText(this.name);
        scene.setLayoutX(this.positionX);
        scene.setLayoutY(this.positionY);
        scene.setMinWidth(this.sizeW);
        scene.setPrefWidth(this.sizeW);
        scene.setMaxWidth(this.sizeW);
        scene.setMinHeight(this.sizeH);
        scene.setPrefHeight(this.sizeH);
        scene.setMaxHeight(this.sizeH);
        this.setReference(scene);

        for (component comp : components_list){
            scene.getChildren().add(comp.compConvert());
        }

        return scene;
    }

    @Override
    public String exportConvert() {
        StringBuilder exportString = new StringBuilder();
        exportString.append("<AnchorPane fx:id=\"" + this.fxid + "\" prefHeight=\""+ this.sizeH +"\" prefWidth=\"" + this.sizeW + "\" layoutX=\"" + this.positionX + "\" layoutY=\"" + this.positionY + "\" >" + "\n");
        exportString.append("<children>" + "\n");
        for (component c : this.components_list) {
            exportString.append(c.exportConvert());
        }
        exportString.append("</children>" + "\n");
        exportString.append("</AnchorPane>");

        return exportString.toString();
    }
    public void refreshComponents(){

    }
}
