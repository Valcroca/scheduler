package DAOImplementation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQueries {

    //Statement reference
    private static Statement statement;

    //Setter - Create statement object
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    //Getter - Return statement object
    public static Statement getStatement() {
        return statement;
    }
}
