package DAOImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String IPAddress = "//3.227.166.251/U05K5A";

    // JDBC URL concatenation
    private static final String jdbcUrl = protocol + vendorName + IPAddress;

    // Driver interface connection reference
    private static final String mysqlJdbcDriver = "com.mysql.jdbc.Driver";
    private static Connection connection = null;

    // DB User Name
    private static final String userName = "U05K5A";

    // DB Password
    private static String password = "53688525130";

    // DB connection setup
    public static Connection startConnection() {
        try {
            Class.forName(mysqlJdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection Closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
