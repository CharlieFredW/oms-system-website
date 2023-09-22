package OMS.data.Database;

import OMS.domain.OrderCRUD.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OrderDatabase implements IDatabase {

    private static OrderDatabase instance;
    public Connection connection = null;

    private OrderDatabase() {
        initializePostgresqlDatabase();
    }

    //create instance that main can use to test database
    public static OrderDatabase getInstance() {
        if (instance == null) {
            instance = new OrderDatabase();
        }
        return instance;
    }

    //make connection to database
    private void initializePostgresqlDatabase() {

        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            String DB_PASSWORD = "postgres";
            String DB_USER = "postgres";
            String DB_NAME = "oms";
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) System.exit(-1);
        }

    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
