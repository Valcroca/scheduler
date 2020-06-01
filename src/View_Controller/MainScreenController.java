package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private RadioButton radioBtnViewWeek;

    @FXML
    private ToggleGroup viewBy;

    @FXML
    private RadioButton radioBtnViewMonth;

    @FXML
    private RadioButton radioBtnViewAll;

    @FXML
    private TableView<?> appointmentsTable;

    @FXML
    private TableColumn<?, ?> appointmentIdColumn;

    @FXML
    private TableColumn<?, ?> appointmentTitleColumn;

    @FXML
    private TableColumn<?, ?> appointmentTypeColumn;

    @FXML
    private TableColumn<?, ?> appointmentStartColumn;

    @FXML
    private TableColumn<?, ?> appointmentEndColumn;

    @FXML
    private Button newAppointmenttBtn;

    @FXML
    private Button updateAppointmentBtn;

    @FXML
    private Button deleteAppointmentBtn;

    @FXML
    private TableView<?> customersTable;

    @FXML
    private TableColumn<?, ?> customerIdColumn;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private TableColumn<?, ?> customerAddressColumn;

    @FXML
    private Button newCustomerBtn;

    @FXML
    private Button deleteCustomerBtn;

    @FXML
    private Button updateCustomerBtn;

    @FXML
    void deleteAppointmentHandler(ActionEvent event) {

    }

    @FXML
    void deleteCustomerHandler(ActionEvent event) {

    }

    @FXML
    void newAppointmentHandler(ActionEvent event) {

    }

    @FXML
    void newCustomerHandler(ActionEvent event) {

    }

    @FXML
    void updateAppointmentHandler(ActionEvent event) {

    }

    @FXML
    void updateCustomerHandler(ActionEvent event) {

    }

    @FXML
    void viewAllHandler(ActionEvent event) {

    }

    @FXML
    void viewMonthHandler(ActionEvent event) {

    }

    @FXML
    void viewWeekHandler(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
