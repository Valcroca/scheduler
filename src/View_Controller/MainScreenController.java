package View_Controller;

import DAOImplementation.DBConnection;
import Model.Appointment;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentStartColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentEndColumn;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomer;

    @FXML
    private Button newAppointmentBtn;

    @FXML
    private Button updateAppointmentBtn;

    @FXML
    private Button deleteAppointmentBtn;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerCountryColumn;

    @FXML
    private TableColumn<Customer, String> customerCityColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;


    @FXML
    private Button newCustomerBtn;

    @FXML
    private Button deleteCustomerBtn;

    @FXML
    private Button updateCustomerBtn;

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        radioBtnViewAll.setSelected(true);
        populateAppointmentsTable();
        populateCustomersTable();
    }

    //populate the appointments table with DB data
    public void populateAppointmentsTable() {
        appointmentsTable.getItems().setAll(getAppointments());
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    //GET appointments from the DB depending on all/weekly/monthly selection
    public ObservableList<Appointment> getAppointments() {

        if (radioBtnViewAll.isSelected()) {
            System.out.println("Finding Appointments");
            allAppointments.clear();

            try (PreparedStatement statement = DBConnection.startConnection().prepareStatement("SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.start, appointment.end FROM appointment;");
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("appointment.appointmentId");
                    int customerId = rs.getInt("appointment.customerId");
                    String title = rs.getString("appointment.title");
                    String type = rs.getString("appointment.type");
                    String start = rs.getString("appointment.start");
                    String end = rs.getString("appointment.end");
                    allAppointments.add(new Appointment(id, customerId, title, type, start, end));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return allAppointments;

        }else if (radioBtnViewMonth.isSelected()) {
            System.out.println("Finding Appointments by month.");
            allAppointments.clear();

            String startMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endMonth = LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            try (PreparedStatement statement = DBConnection.startConnection().prepareStatement("SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.start, appointment.end FROM appointment WHERE appointment.start >= '" + startMonth + "' AND appointment.end <= '" + endMonth + "';");
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("appointment.appointmentId");
                    int customerId = rs.getInt("appointment.customerId");
                    String title = rs.getString("appointment.title");
                    String type = rs.getString("appointment.type");
                    String start = rs.getString("appointment.start");
                    String end = rs.getString("appointment.end");
                    allAppointments.add(new Appointment(id, customerId, title, type, start, end));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return allAppointments;

        } else if (radioBtnViewWeek.isSelected()) {
            System.out.println("Finding Appointments by week.");
            allAppointments.clear();

            String startWeek = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endWeek = LocalDateTime.now().plusWeeks(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            try (PreparedStatement statement = DBConnection.startConnection().prepareStatement("SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.start, appointment.end FROM appointment WHERE appointment.start >= '" + startWeek + "' AND appointment.end <= '" + endWeek + "';");
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("appointment.appointmentId");
                    int customerId = rs.getInt("appointment.customerId");
                    String title = rs.getString("appointment.title");
                    String type = rs.getString("appointment.type");
                    String start = rs.getString("appointment.start");
                    String end = rs.getString("appointment.end");
                    allAppointments.add(new Appointment(id, customerId, title, type, start, end));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return allAppointments;
        } else {
            return allAppointments;
        }
    }

    //populate the customers table with DB data
    public void populateCustomersTable() {
        customersTable.getItems().setAll(getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerCityColumn.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
    }

    //GET all customers from the DB
    public static ObservableList<Customer> getAllCustomers() {
        System.out.println("Finding all Customers");
        allCustomers.clear();

        try (PreparedStatement statement = DBConnection.startConnection().prepareStatement(
                "SELECT customer.customerId, customer.customerName, customer.addressId, address.address, address.postalCode, city.cityId, city.city, country.country, address.phone "
                        + "FROM customer, address, city, country "
                        + "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId;");
             ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("customer.customerId");
                    String name = rs.getString("customer.customerName");
                    String address = rs.getString("address.address");
                    int addressId = rs.getInt("customer.addressId");
                    String city = rs.getString("city.city");
                    String country = rs.getString("country.country");
                    String phone = rs.getString("address.phone");
                    allCustomers.add(new Customer(id, name, addressId, address, city, country, phone));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    //DELETE appointment record
    @FXML
    void deleteAppointmentHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Appointment appointmentToDelete = appointmentsTable.getSelectionModel().getSelectedItem();

            try {
                PreparedStatement ps = DBConnection.startConnection().prepareStatement("DELETE appointment.* FROM appointment " +
                        "WHERE appointment.appointmentId = ?;");
                ps.setInt(1, appointmentToDelete.getAppointmentId());
                ps.execute();
                //update appointment table
                allAppointments.remove(appointmentToDelete);
                populateAppointmentsTable();
                appointmentsTable.refresh();

                //confirm rows affected
                if (ps.getUpdateCount() > 0)
                    System.out.println(ps.getUpdateCount() + " row(s) affected");
                else
                    System.out.println("No change");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //DELETE customer record
    @FXML
    void deleteCustomerHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Are you sure you want to delete this customer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Customer customerToDelete = customersTable.getSelectionModel().getSelectedItem();

            try {
                PreparedStatement ps = DBConnection.startConnection().prepareStatement("DELETE customer.*, address.* FROM customer, address " +
                        "WHERE customer.customerId = ? AND customer.addressId = address.addressId");
                ps.setInt(1, customerToDelete.getCustomerId());
                ps.execute();
                //update customer table
                allCustomers.remove(customerToDelete);
                populateCustomersTable();
                customersTable.refresh();

                //confirm rows affected
                if (ps.getUpdateCount() > 0)
                    System.out.println(ps.getUpdateCount() + " row(s) affected");
                else
                    System.out.println("No change");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void newAppointmentHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) newAppointmentBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddAppointmentScreen.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void newCustomerHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) newCustomerBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddCustomerScreen.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void updateAppointmentHandler(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        Stage stage;
        Parent root;
        stage = (Stage) updateAppointmentBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/UpdateAppointmentScreen.fxml"));
        root = loader.load();
        UpdateAppointmentScreenController controller = loader.getController();
        controller.populateAppointmentFields(selectedAppointment);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void updateCustomerHandler(ActionEvent event) throws IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        Stage stage;
        Parent root;
        stage = (Stage) updateCustomerBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/UpdateCustomerScreen.fxml"));
        root = loader.load();
        UpdateCustomerScreenController controller = loader.getController();
        controller.populateCustomerFields(selectedCustomer);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void viewAllHandler(ActionEvent event) {
        populateAppointmentsTable();
    }

    @FXML
    void viewMonthHandler(ActionEvent event) {
        populateAppointmentsTable();
    }

    @FXML
    void viewWeekHandler(ActionEvent event) {
        populateAppointmentsTable();
    }


}
