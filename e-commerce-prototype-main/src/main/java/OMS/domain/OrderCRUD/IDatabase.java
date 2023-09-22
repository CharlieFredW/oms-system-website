package OMS.domain.OrderCRUD;

import java.sql.Connection;

public interface IDatabase {

    //get connection
    Connection getConnection();
}
