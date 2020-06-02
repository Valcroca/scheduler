package Main;

import DAOImplementation.DBConnection;
import DAOImplementation.DBQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/LoginScreen.fxml"));
        primaryStage.setTitle("Software II");
        primaryStage.setScene(new Scene(root, 685, 503));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {

        Connection conn = DBConnection.startConnection();
        DBQueries.setStatement(conn);
        Statement statement = DBQueries.getStatement();

        //SQL insert statement
        //String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES('US', '2020-02-22 00:00:00', 'admin', 'admin')";

        //variable insert
//        String countryName = "Canada";
//        String createDate = "2020-02-22 00:00:00";
//        String createdBy = "admin";
//        String lastUpdateBy = "admin";
//
//        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy)" +
//                "VALUES(" + "'" + countryName + "', " + "'" + createDate + "', " + "'" + createdBy + "', " + "'" + createdBy + "')";

        //update statement
 //       String updateStatement = "UPDATE country SET country = 'Japan' WHERE country = 'Canada'";

        //delete statement
 //       String deleteStatement = "DELETE FROM country WHERE country = 'Japan'";

        //execute SQL statement
//        statement.execute(deleteStatement);
//        //confirm rows affected
//        if (statement.getUpdateCount() > 0)
//            System.out.println(statement.getUpdateCount() + " row(s) affected");
//        else
//            System.out.println("No change");

        //select all records from country table
        String selectStatement = "SELECT * FROM country";
        //execute statement
        statement.execute(selectStatement);
        //result set
        ResultSet resultSet = statement.getResultSet();
        //forward scroll resultSet
        while(resultSet.next()) {
            int countryId = resultSet.getInt("countryId");
            String countryName = resultSet.getString("country");
            LocalDate date = resultSet.getDate("createDate").toLocalDate();
            LocalTime time = resultSet.getTime("createDate").toLocalTime();
            String createdBy = resultSet.getString("createdBy");
            LocalDateTime lastUpdate = resultSet.getTimestamp("lastUpdate").toLocalDateTime();
            //printing record
            System.out.println(countryId + " | " + countryName + " | " + date  + " " + time + " | " + createdBy  + " | " + lastUpdate);
        }
        launch(args);
        DBConnection.closeConnection();
    }
}
