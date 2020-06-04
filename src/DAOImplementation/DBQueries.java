package DAOImplementation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DBQueries {

    //Statement reference
    private static PreparedStatement statement;

    //Setter - Create statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    //Getter - Return statement object
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}
