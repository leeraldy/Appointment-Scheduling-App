package controller;

import utils.AppointmentDB;
import utils.ContactDB;
import utils.CustomerDB;
import utils.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddAppointment Class: Handles new appointment screem
 *
 * @author  Hussein Coulibaly
 *
 */


public class AddAppointment implements Initializable {
    Stage stage;
    Parent scene;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TextField appointmentIDText;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private TextField typeTextField;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private DatePicker apptDatePicker;
    @FXML private TextField customerID;
    @FXML private ComboBox<User> userIDComboBox;
    @FXML private Button saveButton;
    @FXML private Button clearButton;


    /**
     * This method cancels an add appointment action
     */

    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {

        Alert cancelAlert = new Alert(Alert.AlertType.WARNING);
        cancelAlert.setHeaderText("Cancellation?");
        cancelAlert.setContentText("Changes you made so far will not be saved");

        Optional<ButtonType> result = cancelAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/AppointmentViewer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /**
     * This method add appointment in the DB
     * @param event by clicking save button appointment is saved
     * @throws IOException returns an error if one is found
     */

    @FXML
    public void addButtonHandler(ActionEvent event) throws IOException {


            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            Contact contact = contactComboBox.getValue();
            String type = typeTextField.getText();
            String customerId = customerID.getText();
            User userID = userIDComboBox.getValue();
            LocalDate apptDate = apptDatePicker.getValue();
            LocalTime startDateTime = startTimeComboBox.getValue();
            LocalTime endDateTime = endTimeComboBox.getValue();


            if (title.isBlank() || description.isBlank() || location.isBlank() || contact == null || type.isBlank() || apptDate == null || startDateTime == null || endDateTime ==null || customerId.isBlank() || userID == null) {

                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setHeaderText("Missing Fields");
                emptyAlert.setContentText("Please all fields are required!");
                emptyAlert.showAndWait();

            }

            else {
                    Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(apptDate, startTimeComboBox.getValue()));
                    Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(apptDate, endTimeComboBox.getValue()));
                    int custID = Integer.parseInt(customerId);


                    if (LocalDateTime.of(apptDate, endTimeComboBox.getValue()).isAfter(LocalDateTime.of(apptDate, startTimeComboBox.getValue()))) {

                        Appointment newAppointment = new Appointment(Integer.parseInt("0"), title, description, location, contact.getContactID(), contact.getContactName(), type, startTime, endTime, custID, userID.getUserID());


                        if (AppointmentDB.checkOverlappedAppointment(newAppointment)) {

                            Alert overlapAlert = new Alert(Alert.AlertType.WARNING);
                            overlapAlert.setHeaderText("Overlapping Appointment Detected");
                            overlapAlert.setContentText("Appointment overlaps with an existing appointment for the selected customer.");
                            overlapAlert.showAndWait();

                        } else {

                            AppointmentDB.addAppointment(title, description, location, type, startTime, endTime, custID, userID.getUserID(), contact.getContactID());

                            Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            successAlert.setHeaderText("Confirmation?");
                            successAlert.setContentText("Appointment added successfully!");
                            successAlert.showAndWait();

                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(getClass().getResource("../view/AppointmentViewer.fxml"));
                            stage.setScene(new Scene(scene));
                            stage.show();
                        }

                    }

                    else {
                        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                        invalidAlert.setHeaderText("Inaccurate Time?");
                        invalidAlert.setContentText("Incorrect time format, please ensure appointment end time must be after appointment start time.");
                        invalidAlert.showAndWait();
                    }

            }
        }

    /**
     * This method generates customer ID when mouse clicked on the table
     *
     * @param event by clicking the customer in the customer table
     */

    @FXML
    public void populateCustomerField(MouseEvent event) {

        customerID.setText(String.valueOf(customerTable.getSelectionModel().getSelectedItem().getCustomerID()));

    }


    /**
     * This method initializes add appointment screen
     * @param url the url
     * @param resourceBundle initializes resource bundle
     * Lambda 3 expression is used restrict user to book appointment on weekends
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apptDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate apptDatePicker, boolean empty) {
                super.updateItem(apptDatePicker, empty);
                setDisable(
                        empty ||
                                apptDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                apptDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                apptDatePicker.isBefore(LocalDate.now()));
                if(apptDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY || apptDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY ||
                        apptDatePicker.isBefore(apptDatePicker.now())) {
                    setStyle("#8c8181");
                }
            }
        });

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDB.getAllCustomers());
        contactComboBox.setItems(ContactDB.getAllContacts());
        userIDComboBox.setItems(UserDB.getAllUsers());


        LocalTime apptStartDateTime = LocalTime.of(8, 0);
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), apptStartDateTime);
        ZonedDateTime zonedStartDateTime = startTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startLocalTime = zonedStartDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime earliestApptStartTime = startLocalTime.toLocalTime();

        LocalTime apptLastStartTime = LocalTime.of(21, 45);
        LocalDateTime localEarlyStart = LocalDateTime.of(LocalDate.now(), apptLastStartTime);
        ZonedDateTime startZonedDateTime = localEarlyStart.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime localStartZonedTime = startZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptLocalStartTime = localStartZonedTime.toLocalTime();

        while (earliestApptStartTime.isBefore(apptLocalStartTime.plusSeconds(1)))
        {
            startTimeComboBox.getItems().add(earliestApptStartTime);
            earliestApptStartTime = earliestApptStartTime.plusMinutes(15);
        }

        LocalTime apptEndDateTime = LocalTime.of(8, 15);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), apptEndDateTime);
        ZonedDateTime zonedEndDateTime = endTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endLocalTime = zonedEndDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime latestApptEndTime = endLocalTime.toLocalTime();

        LocalTime apptLastEndTime = LocalTime.of(22, 0);
        LocalDateTime localEarlyEnd = LocalDateTime.of(LocalDate.now(), apptLastEndTime);
        ZonedDateTime endZonedDateTime = localEarlyEnd.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime localEndZonedTime = endZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptLocalEndTime = localEndZonedTime.toLocalTime();

        while (latestApptEndTime.isBefore(apptLocalEndTime.plusSeconds(1))) {
            endTimeComboBox.getItems().add(latestApptEndTime);
            latestApptEndTime = latestApptEndTime.plusMinutes(15);
        }

    }

}
