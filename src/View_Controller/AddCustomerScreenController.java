package View_Controller;

import Model.Country;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerScreenController implements Initializable {

    @FXML
    private Button saveNewCustomerBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private ChoiceBox<Country> countryDropMenue;

    @FXML
    private TextField cityField;

    @FXML
    private TextField phoneField;

    @FXML
    void cancelHandler(ActionEvent event) { }

    @FXML void saveNewCustomerHandler(ActionEvent event) { }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
