package View_Controller;

import DAOImplementation.DBConnection;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    User user = new User();
//    ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameField;

    public LoginScreenController() {};

    @FXML
    void exitButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Exit Appointment Scheduling App");
        alert.setContentText("Are you sure you want to exit this program?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    void loginButtonHandler(ActionEvent event) throws IOException {
        String userNameInput = userNameField.getText();
        String passwordInput = passwordField.getText();

        //validation
        if (userNameInput.isEmpty() || passwordInput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Error");
            alert.setContentText("User name and Password cannot be blank.");
            alert.showAndWait();
        } else {
            User currentUser = exisitingUser(userNameInput, passwordInput);
            if (currentUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Error");
                alert.setContentText("User name or Password are incorrect.");
                alert.showAndWait();
            } else {
                //go to main screen after successful login
                Stage stage;
                Parent root;
                stage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    User exisitingUser(String userNameInput, String passwordInput) {
        try{
            PreparedStatement ps = DBConnection.startConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            ps.setString(1, userNameInput);
            ps.setString(2, passwordInput);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println(ps.getUpdateCount() + " user found.");
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
            } else {
                System.out.println("Not found.");

                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

