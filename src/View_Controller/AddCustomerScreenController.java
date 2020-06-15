package View_Controller;

import DAOImplementation.DBConnection;
import Model.City;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

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
    private ComboBox<City> cityComboBox;

    @FXML
    private TextField cityField;

    @FXML
    private TextField phoneField;

    private static ObservableList<City> citiesOptions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCities();
        cityComboBox.setItems(citiesOptions);
    }

    @FXML
    void cancelHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) cancelBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML void saveNewCustomerHandler(ActionEvent event) throws IOException {

        if (validatesCustomer()) {
            //CREATE new customer
            try {
                //first create new address
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(
                        "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, addressField.getText());
                ps.setString(2, "none");
                ps.setInt(3, cityComboBox.getValue().getCityId());
                ps.setString(4, "none");
                ps.setString(5, phoneField.getText());
                ps.setString(6, getTimestamp());
                ps.setString(7, "app user");
                ps.setString(8, getTimestamp());
                ps.setString(9, "app user");

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Address creation failed.");
                }
                //retrieve new address Id
                int newAddressId;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        newAddressId = rs.getInt(1);
                    }
                    else {
                        throw new SQLException("Creating new address failed, no ID obtained.");
                    }
                }
                //create new customer
                PreparedStatement psCustomer = DBConnection.startConnection().prepareStatement(
                        "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?);"

                );
                psCustomer.setString(1, nameField.getText());
                psCustomer.setInt(2, newAddressId);
                psCustomer.setInt(3, 1);
                psCustomer.setString(4, getTimestamp());
                psCustomer.setString(5, "app user");
                psCustomer.setString(6, getTimestamp());
                psCustomer.setString(7, "app user");
                int newCustomerRow = psCustomer.executeUpdate();
                if (newCustomerRow == 0) {
                    throw new SQLException("Customer creation failed.");
                } else {
                    //success message alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Success!");
                    alert.setContentText("The new customer was successfully created.");
                    alert.showAndWait();
                    //go back to main screen
                    cancelHandler(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private String getTimestamp() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        return sdf.format(date);
    }

    private static ObservableList<City> getCities() {

        try {
            PreparedStatement statement = DBConnection.startConnection().prepareStatement(
                    "SELECT city.cityId, city.city, city.countryId FROM city;"
            );
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int cityId = rs.getInt("city.cityId");
                String cityName = rs.getString("city.city");
                citiesOptions.add(new City(cityId, cityName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citiesOptions;
    }

    private boolean validatesCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        City city = cityComboBox.getValue();
        String phone = phoneField.getText();
        String errorMessage = "";

        //field validations
        if (name == null || name.isEmpty())
            errorMessage += "The name cannot be blank.\n";

        if (address == null || address.isEmpty())
            errorMessage += "The address cannot be blank.\n";

        if (city == null)
            errorMessage += "Please select a city.\n";

        if (phone == null || phone.isEmpty())
            errorMessage += "The phone number cannot be blank.\n";

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error(s) to correct: ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

    }
}
