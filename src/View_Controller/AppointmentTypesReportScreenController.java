package View_Controller;

import DAOImplementation.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AppointmentTypesReportScreenController implements Initializable {

    @FXML
    private TextArea typesArea;

    @FXML
    private Button backBtn;

    //get the logged-in user's id
    int currentUserId = LoginScreenController.getCurrentUser().getUserId();

    @FXML
    void backBtnHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) backBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getReport() {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT COUNT(DISTINCT type) FROM appointment WHERE userID = "+ currentUserId +";";
            ResultSet results = statement.executeQuery(query);
            StringBuilder apptTypesReport = new StringBuilder();
            apptTypesReport.append(String.format("Total Number of Appointment Types: "));

            while (results.next()) {
                apptTypesReport.append(results.getInt("count(distinct type)")).toString();}
            statement.close();
            typesArea.setText(apptTypesReport.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReport();
    }
}
