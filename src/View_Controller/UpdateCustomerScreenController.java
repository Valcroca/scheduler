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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateCustomerScreenController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<City> cityComboBox;

    @FXML
    private Button updateCustomerBtn;

    Customer customer;
    private static ObservableList<City> citiesOptions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCities();
        cityComboBox.setItems(citiesOptions);
    }

    private static ObservableList<City> getCities() {
        citiesOptions.clear();
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

    public void getSelectedCity(String customerCity) {
        for (City city : citiesOptions) {
            if (city.getCity().equals(customerCity)) {
                cityComboBox.setValue(city);
            }
        }
    }

    public void populateCustomerFields(Customer customer) {
        this.customer = customer;

        nameField.setText(customer.getCustomerName());
        addressField.setText(customer.getCustomerAddress());
        getSelectedCity(customer.getCustomerCity());
        phoneField.setText(customer.getCustomerPhone());
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

    @FXML
    void updateCustomerHandler(ActionEvent event) throws IOException {
        if (validatesCustomer()) {
            //UPDATE customer
            int addressId = customer.getAddressId();
            try {
                //first update address if address, city or phone changed
                if (addressField.getText() != customer.getCustomerAddress() || cityComboBox.getValue().getCity() != customer.getCustomerCity() || phoneField.getText() != customer.getCustomerPhone()) {
                    PreparedStatement ps = DBConnection.startConnection().prepareStatement(
                            "UPDATE address SET address= ?, address2= ?, cityId= ?, postalCode= ?, phone= ?, createDate= ?, createdBy= ?, lastUpdate= ?, lastUpdateBy= ? WHERE addressId= ?;"
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
                    ps.setInt(10, addressId);

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Address update failed.");
                    }
                }
                //UPDATE customer
                PreparedStatement psCustomer = DBConnection.startConnection().prepareStatement(
                        "UPDATE customer SET customerName= ?, active= ?, createDate= ?, createdBy= ?, lastUpdate= ?, lastUpdateBy= ? WHERE customerId=?;"

                );
                psCustomer.setString(1, nameField.getText());
                psCustomer.setInt(2, 1);
                psCustomer.setString(3, getTimestamp());
                psCustomer.setString(4, "app user");
                psCustomer.setString(5, getTimestamp());
                psCustomer.setString(6, "app user");
                psCustomer.setInt(7, customer.getCustomerId());

                int updatedCustomerRow = psCustomer.executeUpdate();
                if (updatedCustomerRow == 0) {
                    throw new SQLException("Customer update failed.");
                } else {
                    //success message alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Success!");
                    alert.setContentText("The new customer was successfully updated.");
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
        Date date = new Date();
        return sdf.format(date);
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
