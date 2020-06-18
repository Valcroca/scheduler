package View_Controller;

import DAOImplementation.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UserScheduleReportScreenController implements Initializable {
    @FXML
    private TextArea scheduleArea;

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
            String query = "SELECT title, start, end FROM appointment WHERE userID = "+ currentUserId +";";
            ResultSet results = statement.executeQuery(query);
            StringBuilder scheduleReport = new StringBuilder();
            scheduleReport.append("Your Complete Schedule: \n\n");
            scheduleReport.append(String.format("%1$-45s %2$-45s %3$-45s \n", "Title", "Start (UTC)", "End (UTC)"));
            scheduleReport.append("\n");
            while (results.next()) {
                scheduleReport.append(String.format("%1$-25s %2$-35s %3$-35s \n", results.getString("title"), results.getString("start"), results.getString("end")));}
            statement.close();
            scheduleArea.setText(scheduleReport.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReport();
    }
}
