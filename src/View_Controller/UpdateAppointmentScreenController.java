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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UpdateAppointmentScreenController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

    @FXML
    private Button updateAppointmentBtn;

    private static ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    Appointment appointment;
    LocalDate startDate;
    LocalTime startTime;
    LocalDate endDate;
    LocalTime endTime;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //format date pickers
        startDatePicker.setDayCellFactory(dayCellFactory);
        endDatePicker.setDisable(true);

        //format time combo boxes
        setBusinessHours();
        startTimeComboBox.setItems(businessHours);
        endTimeComboBox.setItems(businessHours);

        //get customers in combo box
        getAllCustomers();
        customerComboBox.setItems(allCustomers);
    }

    public void getSelectedCustomer(int customerId) {
        for (Customer customer : allCustomers) {

            if (customer.getCustomerId() == customerId) {
                customerComboBox.setValue(customer);
            }
        }
    }

    public void populateAppointmentFields(Appointment appointment) {
        this.appointment = appointment;
        parseEnd();
        parseStart();

        titleField.setText(appointment.getTitle());
        typeField.setText(appointment.getType());
        getSelectedCustomer(appointment.getCustomerId());
        startDatePicker.setValue(startDate);
        startTimeComboBox.setValue(startTime);
        endDatePicker.setValue(endDate);
        endTimeComboBox.setValue(endTime);
    }

    private void parseStart() {
        String startDateTime = appointment.getStart();
        startDate = LocalDate.parse(startDateTime.substring(0, startDateTime.indexOf(' ')));
        startTime = LocalTime.parse(startDateTime.substring(11, 16), DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void parseEnd() {
        String endDateTime = appointment.getEnd();
        endDate = LocalDate.parse(endDateTime.substring(0, endDateTime.indexOf(' ')));
        endTime = LocalTime.parse(endDateTime.substring(11, 16), DateTimeFormatter.ofPattern("HH:mm"));
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
    void startDateSelectedHandler(ActionEvent event) {
        endDatePicker.setValue(startDatePicker.getValue());
    }

    //getting a utc timestamp from the local time zone
    private String getTimestampUTC() {
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        //creating date time string that SQL will accept
        String date = String.valueOf(timestamp.toLocalDate());
        String time = String.valueOf(timestamp.toLocalTime());
        String utcDateTimeString = date + " " + time;
        return utcDateTimeString;
    }

    //parsing start dateTime from local zone to utc
    private String getStartDateTimeInUTC() {
        //get local zone id
        ZoneId currentZoneId = ZoneId.of(TimeZone.getDefault().getID());
        //get local offset
        ZoneOffset offset = ZoneId.of(currentZoneId.toString()).getRules().getOffset(Instant.now());
        //concatenate string from date picker and time combo box to get a dateTime string
        String localDateTimeString = startDatePicker.getValue().toString() + "T" + startTimeComboBox.getValue().toString() + ":00" + offset + "[" + currentZoneId + "]";
        //create a ZonedDateTIme object with that string and convert it to UTC
        ZonedDateTime localDateTime = ZonedDateTime.parse(localDateTimeString);
        Instant localToUtcInstant = localDateTime.toInstant();
        ZonedDateTime utcDateTime = localToUtcInstant.atZone(ZoneOffset.UTC);
        //creating date time string that SQL will accept
        String date = String.valueOf(utcDateTime.toLocalDate());
        String time = String.valueOf(utcDateTime.toLocalTime());
        String utcDateTimeString = date + " " + time;
        return utcDateTimeString;
    }

    //parsing end dateTime from local zone to utc
    private String getEndDateTimeInUTC() {
        //get local zone id
        ZoneId currentZoneId = ZoneId.of(TimeZone.getDefault().getID());
        //get local offset
        ZoneOffset offset = ZoneId.of(currentZoneId.toString()).getRules().getOffset(Instant.now());
        //concatenate string from date picker and time combo box to get a dateTime string
        String localDateTimeString = endDatePicker.getValue().toString() + "T" + endTimeComboBox.getValue().toString() + ":00" + offset + "[" + currentZoneId + "]";
        //create a ZonedDateTIme object with that string and convert it to UTC
        ZonedDateTime localDateTime = ZonedDateTime.parse(localDateTimeString);
        Instant localToUtcInstant = localDateTime.toInstant();
        ZonedDateTime utcDateTime = localToUtcInstant.atZone(ZoneOffset.UTC);
        //creating date time string that SQL will accept
        String date = String.valueOf(utcDateTime.toLocalDate());
        String time = String.valueOf(utcDateTime.toLocalTime());
        String utcDateTimeString = date + " " + time;
        return utcDateTimeString;
    }

    @FXML
    void updateAppointmentHandler(ActionEvent event) throws IOException {
        if (validatesAppointment()) {
            try {
                //update appointment
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(
                        "UPDATE appointment SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, type=?, url=?, start=?, end=?, createDate=?, createdBy=?, lastUpdate=?, lastUpdateBy=? WHERE appointmentId=?");

                ps.setInt(1, customerComboBox.getValue().getCustomerId());
                ps.setInt(2, 1);
                ps.setString(3, titleField.getText());
                ps.setString(4, "default");
                ps.setString(5, "default");
                ps.setString(6, "default");
                ps.setString(7, typeField.getText());
                ps.setString(8, "default");
                ps.setString(9, getStartDateTimeInUTC());
                ps.setString(10, getEndDateTimeInUTC());
                ps.setString(11, getTimestampUTC());
                ps.setString(12, "app user");
                ps.setString(13, getTimestampUTC());
                ps.setString(14, "app user");
                ps.setInt(15, appointment.getAppointmentId());

                int updatedAppointmentRow = ps.executeUpdate();
                if (updatedAppointmentRow == 0) {
                    throw new SQLException("Appointment update failed.");
                } else {
                    //success message alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Success!");
                    alert.setContentText("The appointment was successfully updated.");
                    alert.showAndWait();
                    //go back to main screen
                    cancelHandler(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty)
                {
                    // Must call super
                    super.updateItem(item, empty);

                    // Show Weekends in gray color
                    DayOfWeek day = DayOfWeek.from(item);
                    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                    {
                        this.setTextFill(Color.GRAY);
                        this.setDisable(true);
                    }

                    // Disables dates in the past
                    if (item.isBefore(LocalDate.now())
                            ) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            };
        }
    };

    //setting business hours
    public static ObservableList<LocalTime> setBusinessHours() {
        businessHours.clear();
        businessHours.add(0, LocalTime.parse("09:00"));
        businessHours.add(1, LocalTime.parse("09:15"));
        businessHours.add(2, LocalTime.parse("09:30"));
        businessHours.add(3, LocalTime.parse("09:45"));
        businessHours.add(4, LocalTime.parse("10:00"));
        businessHours.add(5, LocalTime.parse("10:15"));
        businessHours.add(6, LocalTime.parse("10:30"));
        businessHours.add(7, LocalTime.parse("10:45"));
        businessHours.add(8, LocalTime.parse("11:00"));
        businessHours.add(9, LocalTime.parse("11:15"));
        businessHours.add(10, LocalTime.parse("11:30"));
        businessHours.add(11, LocalTime.parse("11:45"));
        businessHours.add(12, LocalTime.parse("14:00"));
        businessHours.add(13, LocalTime.parse("14:15"));
        businessHours.add(14, LocalTime.parse("14:30"));
        businessHours.add(15, LocalTime.parse("14:45"));
        businessHours.add(16, LocalTime.parse("15:00"));
        businessHours.add(17, LocalTime.parse("15:15"));
        businessHours.add(18, LocalTime.parse("15:30"));
        businessHours.add(19, LocalTime.parse("15:45"));

        return businessHours;
    }

    private static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            PreparedStatement statement = DBConnection.startConnection().prepareStatement(
                    "SELECT customer.customerId, customer.customerName FROM customer;"
            );
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("customer.customerId");
                String customerName = rs.getString("customer.customerName");
                allCustomers.add(new Customer(customerId, customerName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    private boolean isOverlappingAppt() {
        boolean isOverlapping = true;
        String start = getStartDateTimeInUTC() + ":00";
        String end = getEndDateTimeInUTC() + ":00";

        //check the db for any appointments that have the same user, exclude the current appointment it's being edited, and have start or end datetimes between the selected start and datetimes from the update page.
        try {
            PreparedStatement statement = DBConnection.startConnection().prepareStatement(
                    "SELECT * FROM appointment WHERE userId = 1 AND NOT appointmentId = " + appointment.getAppointmentId() + " AND start BETWEEN '" + start + "' AND '" + end + "'OR end BETWEEN '" + start + "' AND '" + end + "';"
            );
            ResultSet rs = statement.executeQuery();
            //if we have results, then we have an overlapping appt
            if (rs.next()) {
                isOverlapping = true;
            } else
                isOverlapping = false;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isOverlapping;
    }

    private boolean validatesAppointment() {
        String title = titleField.getText();
        String type = typeField.getText();
        Customer customer = customerComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getSelectionModel().getSelectedItem();
        String errorMessage = "";
        boolean passesValidations;

        //validations
        if (title == null || title.isEmpty())
            errorMessage += "Title cannot be empty.\n";

        if (type == null || type.isEmpty())
            errorMessage += "Type cannot be empty.\n";

        if (customer == null)
            errorMessage += "Please select a customer.\n";

        if (startDate == null)
            errorMessage += "Please select a Start Date.\n";

        if (startTime == null)
            errorMessage += "Please select a Start Time.\n";

        if (endDate == null)
            errorMessage += "Please select a End Date.\n";

        if (endTime == null)
            errorMessage += "Please select a End Time.\n";

        if (endTime != null && startTime != null) {
            if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                errorMessage += "End Time cannot be at the same time or before the Start Time.\n";
            }

            if (startDate.equals(LocalDate.now())) {
                if (startTime.isBefore(LocalTime.now()))
                    errorMessage += "Start Time cannot be in the past.";
            }
            //validation for overlapping appointments for the user
            if (startDate != null && endDate != null) {
                if (isOverlappingAppt()) {
                    errorMessage += "There is already an appointment at the selected time. Please choose a different time.";
                }
            }
        }

        if (errorMessage.isEmpty())
            passesValidations = true;
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error(s) to correct: ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            passesValidations = false;
        }

        return passesValidations;
    }
}
