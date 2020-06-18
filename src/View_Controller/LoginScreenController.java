package View_Controller;

import DAOImplementation.DBConnection;
import Model.LoginLogger;
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


    @FXML
    private Label userNameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameField;

    static User currentUser;
    private String errorText, errorText1, errorHeader, exitMessage, exitHeader;

    public LoginScreenController() {}

    @FXML
    void exitButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(exitHeader);
        alert.setContentText(exitMessage);

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
            alert.setTitle(errorHeader);
            alert.setHeaderText("Error");
            alert.setContentText(errorText1);
            alert.showAndWait();
        } else {
            currentUser = existingUser(userNameInput, passwordInput);
            if (currentUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(errorHeader);
                alert.setHeaderText("Error");
                alert.setContentText(errorText);
                alert.showAndWait();
            } else {
                //log successful login
                LoginLogger.log(userNameInput);
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

    User existingUser(String userNameInput, String passwordInput) {
        try{
            PreparedStatement ps = DBConnection.startConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            ps.setString(1, userNameInput);
            ps.setString(2, passwordInput);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = new User();
                System.out.println(ps.getUpdateCount() + " user found.");
                currentUser.setUserId(rs.getInt("userId"));
                currentUser.setUserName(rs.getString("userName"));
                currentUser.setPassword(rs.getString("password"));
            } else {
                System.out.println("Not found.");

                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Languages/lang", locale);
        errorText = rb.getString("errorText");
        errorText1 =  rb.getString("errorText1");
        errorHeader = rb.getString("errorHeader");
        exitMessage = rb.getString("exitMessage");
        exitHeader = rb.getString("exitHeader");
        userNameLabel.setText(rb.getString("user"));
        passwordLabel.setText(rb.getString("password"));
        exitButton.setText(rb.getString("exit"));
        loginLabel.setText(rb.getString("login"));
        loginButton.setText(rb.getString("login"));
    }
}

