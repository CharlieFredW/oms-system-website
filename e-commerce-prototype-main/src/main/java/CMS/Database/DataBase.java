package CMS.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private String url;
    private int port;
    private String databaseName;
    private String username;
    private String password;
    private Connection connection;

    public DataBase(String url, int port, String databaseName, String username, String password) {
        this.url = url;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        this.connection = null;

        connectToDB();

    }

    public void connectToDB() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" +
                            this.url +
                            ":" +
                            this.port +
                            "/" +
                            this.databaseName,
                    this.username,
                    this.password
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }
}
