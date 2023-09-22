package CMS.Logic;

import CMS.Logic.component.component;
import CMS.Database.DataBase;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class File {
    private static File currentFile;
    private static boolean isOpening = false;
    private static boolean isSaved;
    private String fileName;
    private String containerType;
    private String wrapperType;
    private int viewport_width;
    private int viewport_height;
    private String exportPath;
    private Writer writer;
    private ArrayList<component> componentsList;

    // New file constructor
    public File (String fileName, String containerType, String wrapperType, int viewport_width, int viewport_height) {
        this.fileName = fileName;
        this.containerType = containerType;
        this.wrapperType = wrapperType;
        this.viewport_width = viewport_width;
        this.viewport_height = viewport_height;
        this.exportPath = "export/" + fileName + ".fxml";
        this.componentsList = new ArrayList<>();
    }
    // Existing file constructor
    public File(String name, String containerType, String wrapperType, int viewport_width, int viewport_height, ArrayList<component> content) {
        this.fileName = name;
        this.containerType = containerType;
        this.wrapperType = wrapperType;
        this.viewport_width = viewport_width;
        this.viewport_height = viewport_height;
        this.exportPath = "export/" + name + ".fxml";
        this.componentsList = content;
    }

    public void export() {
        StringBuilder exportString = new StringBuilder();
        // FXML header and imports
        exportString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
        exportString.append("<?import javafx.scene.control.*?>" + "\n");
        exportString.append("<?import javafx.scene.layout.*?>" + "\n");
        exportString.append("<?import javafx.scene.image.ImageView?>" + "\n");
        exportString.append("<?import javafx.scene.image.Image?>" + "\n");
        exportString.append("<?import javafx.scene.text.Font?>" + "\n");
        // FXML body
        if(!this.wrapperType.equals("None")) {
            if(!(this.wrapperType.equals("HBox") || this.wrapperType.equals("VBox"))) {
                if(this.wrapperType.equals("HBox+VBox")){
                    exportString.append("<HBox id=\"" + this.fileName + "\" prefHeight=\"" + this.viewport_height + "\" prefWidth=\"" + this.viewport_width + "\" xmlns=\"http://javafx.com/javafx/19\" xmlns:fx=\"http://javafx.com/fxml/1\" >" + "\n");
                    exportString.append("<children>" + "\n");
                    exportString.append("<VBox>" + "\n");
                }
                if(this.wrapperType.equals("VBox+HBox")){
                    exportString.append("<VBox id=\"" + this.fileName + "\" prefHeight=\"" + this.viewport_height + "\" prefWidth=\"" + this.viewport_width + "\" xmlns=\"http://javafx.com/javafx/19\" xmlns:fx=\"http://javafx.com/fxml/1\" >" + "\n");
                    exportString.append("<children>" + "\n");
                    exportString.append("<HBox>" + "\n");
                }
            } else {
                exportString.append("<" + this.wrapperType + " id=\"" + this.fileName + "\" prefHeight=\"" + this.viewport_height + "\" prefWidth=\"" + this.viewport_width + "\" xmlns=\"http://javafx.com/javafx/19\" xmlns:fx=\"http://javafx.com/fxml/1\" >" + "\n");
            }
            exportString.append("<children>" + "\n");
        }

        exportString.append("<" + containerType + " id=\"" + this.fileName + "\" prefHeight=\""+ this.viewport_height +"\" prefWidth=\"" + this.viewport_width + "\" xmlns=\"http://javafx.com/javafx/19\" xmlns:fx=\"http://javafx.com/fxml/1\" >" + "\n");

        if(containerType.equals("ScrollPane")) { exportString.append("<content>" + "\n"); } else { exportString.append("<children>" + "\n"); }
        for (component c : componentsList) {
            exportString.append(c.exportConvert());
            exportString.append("\n");
        }
        if(containerType.equals("ScrollPane")) { exportString.append("</content>" + "\n"); } else { exportString.append("</children>" + "\n"); }
        exportString.append("</" + containerType + ">");
        if(!this.wrapperType.equals("None")) {
            if(!(this.wrapperType.equals("HBox") || this.wrapperType.equals("VBox"))) {
                exportString.append("</children>");
                if(this.wrapperType.equals("HBox+VBox")){
                    exportString.append("</VBox>" + "\n");
                    exportString.append("</children>" + "\n");
                    exportString.append("</HBox>");
                }
                if(this.wrapperType.equals("VBox+HBox")){
                    exportString.append("</HBox>" + "\n");
                    exportString.append("</children>" + "\n");
                    exportString.append("</VBox>");
                }
            } else {
                exportString.append("</" + this.wrapperType + ">");
            }
        }
        System.out.println(exportString);

        // Write to file
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportPath), "utf-8"))) {
            writer.write(exportString.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {writer.close();} catch (Exception e) {/*ignore*/}
        }
    }
    public static boolean isUniqueFileName(String name, DataBase dataBase) {
        try {
            PreparedStatement ps = dataBase.getConnection().prepareStatement("SELECT * FROM files WHERE filename = \'" + name + "\';");
            System.out.println(ps.toString());
            ResultSet rs = dataBase.getConnection().prepareStatement(ps.toString()).executeQuery();
            if(!rs.isBeforeFirst()) {
                return true;
            }
            else { return false; }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateExportPath(){
        this.exportPath = "export/" + this.fileName + ".fxml";
    }
    @Override
    public String toString(){
        return fileName;
    }
    public static File getCurrentFile() { return currentFile; }
    public static boolean isOpening() { return isOpening; }
    public static boolean isSaved() { return isSaved; }

    public String getFileName() { return fileName; }

    public String getContainerType() { return containerType; }

    public String getWrapperType() { return wrapperType; }

    public int getViewport_width() {
        return viewport_width;
    }

    public int getViewport_height() {
        return viewport_height;
    }

    public String getExportPath() {
        return exportPath;
    }

    public ArrayList<component> getComponentsList() {
        return componentsList;
    }
    public static void setCurrentFile(File file) { currentFile = file; }
    public static void setIsOpening(boolean b) { isOpening = b; }
    public static void setIsSaved(boolean b) { isSaved = b; }

    public void setFileName(String name) {
        this.fileName = name;
        updateExportPath();
    }
    public void setContainerType(String containerType) { this.containerType = containerType; }
    public void setWrapperType(String wrapperType) { this.wrapperType = wrapperType; }

    public void setViewport_width(int viewport_width) {
        this.viewport_width = viewport_width;
    }

    public void setViewport_height(int viewport_height) {
        this.viewport_height = viewport_height;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public void setComponentsList(ArrayList<component> componentsList) {
        this.componentsList = componentsList;
    }

}
