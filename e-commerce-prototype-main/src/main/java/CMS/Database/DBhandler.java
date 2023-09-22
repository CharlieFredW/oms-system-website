package CMS.Database;

import CMS.Logic.File;
import CMS.Logic.component.ContainsText;
import CMS.Logic.component.component;
import CMS.Logic.component.subscene;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBhandler {

/** public methods **/
    public ResultSet getAllFromDB (
            DataBase dataBase,
            String tableName
    ){
        try {
            StringBuilder getRSet = new StringBuilder();
            getRSet.append("SELECT * FROM ");
            getRSet.append(tableName);
            getRSet.append(";");
            PreparedStatement preparedStatement1 = dataBase.getConnection().prepareStatement(getRSet.toString());
            ResultSet resultSet = preparedStatement1.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println(tableName + " table not found");
            return null;
        }
    }
    public void clearAndSaveComponents(
            DataBase dataBase,
            ArrayList<component> arrayListOfComponents,
            String tableName
    ){
        clearTable(dataBase, tableName);
        saveComponents(dataBase, arrayListOfComponents, tableName);
    }
    public boolean addNewFile(
            DataBase dataBase,
            File file)
    {
        try {
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement(
                    "INSERT INTO files(" +
                            "id, " +
                            "filename, " +
                            "viewport_width, " +
                            "viewport_height, " +
                            "exportpath, " +
                            "containertype," +
                            "wrappertype" +
                            ") VALUES (" +
                            "?, ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setInt(1, getLengthOfTable(dataBase, "files") + 1);
            preparedStatement.setString(2, file.getFileName());
            preparedStatement.setInt(3, file.getViewport_width());
            preparedStatement.setInt(4, file.getViewport_height());
            preparedStatement.setString(5, file.getExportPath());
            preparedStatement.setString(6, file.getContainerType());
            preparedStatement.setString(7, file.getWrapperType());

            System.out.println(preparedStatement);
            preparedStatement.execute();
            System.out.println("New file added to database");

            if(addNewComponentsTable(dataBase, file.getFileName())) {
                System.out.println("New table added to database");
                return true;
            } else {
                return false;
            }


        } catch (SQLException e) {
            return false;
        }
    }
    public boolean updateFile(DataBase dataBase, String newFileName, String oldFileName){
        try {
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement("UPDATE FILES SET " +
                    "filename = ?, " +
                    "viewport_width = ?," +
                    "viewport_height = ?," +
                    "exportpath = ?," +
                    "containertype = ?," +
                    "wrappertype = ?" +
                    " WHERE filename = ?;");
            preparedStatement.setString(1, newFileName);
            preparedStatement.setInt(2, File.getCurrentFile().getViewport_width());
            preparedStatement.setInt(3, File.getCurrentFile().getViewport_height());
            preparedStatement.setString(4, File.getCurrentFile().getExportPath());
            preparedStatement.setString(5, File.getCurrentFile().getContainerType());
            preparedStatement.setString(6, File.getCurrentFile().getWrapperType());
            preparedStatement.setString(7, oldFileName);
            preparedStatement.execute();
            System.out.println("File updated in database");
            if(!newFileName.equals(oldFileName)) {
                if(renameComponentsTable(dataBase, newFileName, oldFileName)){
                    System.out.println("Table renamed in database");
                } else {
                    System.out.println("Table rename failed");
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean deleteFile(DataBase dataBase, String fileName){
        try {
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement(
                    "DELETE FROM files WHERE filename = ?;"
            );
            preparedStatement.setString(1, fileName);
            preparedStatement.execute();
            System.out.println("File deleted from database");
            if(deleteComponentsTable(dataBase, fileName)) {
                System.out.println("Table deleted from database");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

/** private methods **/
    public void clearTable (
            DataBase dataBase,
            String tableName
    ){
        try {
            StringBuilder cleaner = new StringBuilder();
            cleaner.append("DELETE FROM ");
            cleaner.append(tableName);
            cleaner.append(" WHERE id > -1;");
            PreparedStatement preparedStatement1 = dataBase.getConnection().prepareStatement(cleaner.toString());
            preparedStatement1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addComponentToTable (
            DataBase dataBase,
            component c,
            String tableName
    ){
        try {

            int tableLengthPlusOne = getLengthOfTable(dataBase, tableName) + 1;

            StringBuilder sqlAdd = new StringBuilder();
            sqlAdd.append("INSERT INTO ");
            sqlAdd.append(tableName);
            sqlAdd.append(" (");
            sqlAdd.append("id, ");
            sqlAdd.append("type_id, ");
            sqlAdd.append("components_name, ");
            sqlAdd.append("components_x, ");
            sqlAdd.append("components_y, ");
            sqlAdd.append("components_width, ");
            sqlAdd.append("components_height, ");
            sqlAdd.append("components_color, ");
            sqlAdd.append("components_text_content, ");
            sqlAdd.append("fxid) ");
            sqlAdd.append("VALUES (");
            sqlAdd.append( tableLengthPlusOne + ", ");
            sqlAdd.append( c.getTypeID() + ", ");
            sqlAdd.append( "\'" + c.getName() + "\'" + ", ");
            sqlAdd.append( c.getPositionX() + ", ");
            sqlAdd.append( c.getPositionY() + ", ");
            sqlAdd.append( c.getSizeW() + ", ");
            sqlAdd.append( c.getSizeH() + ", ");
            sqlAdd.append( "\'" + c.colorTrim(c.getColor()) + "\', " );
            if (c instanceof ContainsText) {
                if (((ContainsText) c).getTextContent() == null){
                    sqlAdd.append(((ContainsText) c).getTextContent());
                } else {
                    sqlAdd.append("\'" + ((ContainsText) c).getTextContent() + "\'");
                }
            } else {
                sqlAdd.append("null");
            }
            sqlAdd.append(", \'" + c.getFxid() + "\'" );
            sqlAdd.append(");");

            System.out.println(sqlAdd);

            PreparedStatement preparedStatement2 = dataBase.getConnection().prepareStatement(sqlAdd.toString());
            preparedStatement2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int getLengthOfTable (
            DataBase dataBase,
            String tableName
    ){
        try {
            StringBuilder sqlGetLength = new StringBuilder();
            sqlGetLength.append("SELECT COUNT(*) FROM ");
            sqlGetLength.append(tableName);
            sqlGetLength.append(";");
            PreparedStatement preparedStatement1 = dataBase.getConnection().prepareStatement(sqlGetLength.toString());
            ResultSet resultSet = preparedStatement1.executeQuery();
            int tableLength = 0;
            if (resultSet.next()){
                tableLength = resultSet.getInt(1);
            }

            return tableLength;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    private void saveComponents(
            DataBase dataBase,
            ArrayList<component> arrayListOfComponents,
            String tableName
    ){
        for (component c : arrayListOfComponents) {
            addComponentToTable(dataBase, c, tableName);
        }
    }
    private void saveSubSceneComponents(
            DataBase dataBase,
            subscene subSceneComponent
    ){
        String tableName = subSceneComponent.getName();
        ArrayList<component> arrayListOfComponents = subSceneComponent.getComponents_list();
        for (component c : arrayListOfComponents) {
            if (c.getTypeID() == 1){
                saveSubSceneComponents(dataBase, (subscene) c);
            } else {
                if (checkIfTableExist(dataBase, tableName)){
                    clearTable(dataBase, tableName);
                } else {
                    createNewTableForComponents(dataBase, tableName);
                }
                addComponentToTable(dataBase, c, tableName);
            }
        }
    }
    private boolean checkIfTableExist(
            DataBase dataBase,
            String tableName
    ){
        try {
            Boolean existing;
            StringBuilder checkIfExist = new StringBuilder();
            checkIfExist.append("SELECT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = \'");
            checkIfExist.append(tableName);
            checkIfExist.append("\');");
            PreparedStatement preparedStatement1 = dataBase.getConnection().prepareStatement(checkIfExist.toString());
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (resultSet.next()){
                existing = resultSet.getBoolean(1);
                return existing;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void createNewTableForComponents(
            DataBase dataBase,
            String tableName
    ){
        try {
            StringBuilder createNewTable = new StringBuilder();
            createNewTable.append("CREATE TABLE ");
            createNewTable.append(tableName);
            createNewTable.append(" (");
            createNewTable.append("id int primary key NOT NULL, ");
            createNewTable.append("type_id int NOT NULL, ");
            createNewTable.append("components_name varchar(50) NOT NULL, ");
            createNewTable.append("components_x int NOT NULL, ");
            createNewTable.append("components_y int NOT NULL, ");
            createNewTable.append("components_width int NOT NULL, ");
            createNewTable.append("components_height int NOT NULL, ");
            createNewTable.append("components_color varchar(10), ");
            createNewTable.append("components_text_content varchar(500)");
            createNewTable.append(");");
            PreparedStatement preparedStatement1 = dataBase.getConnection().prepareStatement(createNewTable.toString());
            preparedStatement1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean addNewComponentsTable(
            DataBase dataBase,
            String tableName)
    {
        try {
            StringBuilder AddNewComponentsTable = new StringBuilder();
            AddNewComponentsTable.append("CREATE TABLE ");
            AddNewComponentsTable.append(tableName);
            AddNewComponentsTable.append(" (id SERIAL PRIMARY KEY, fxid VARCHAR(255), type_id INTEGER, components_name VARCHAR(255), components_x INTEGER, components_y INTEGER, components_width INTEGER, components_height INTEGER, components_color VARCHAR(255), components_text_content VARCHAR(255));");
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement(AddNewComponentsTable.toString());
            System.out.println(preparedStatement);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    private boolean renameComponentsTable(DataBase dataBase, String newName, String oldName){
        try {
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement("ALTER TABLE " + oldName + " RENAME TO " + newName + ";");
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    private boolean deleteComponentsTable(
            DataBase dataBase,
            String tableName
    ){
        try {
            PreparedStatement preparedStatement = dataBase.getConnection().prepareStatement("DROP TABLE ?;");
            preparedStatement.setString(1, tableName);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
