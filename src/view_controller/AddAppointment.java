package view_controller;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * AddAppointment Class: Handles new appointment addition
 *
 * @author Hussein Coulibaly
 */
public class AddAppointment implements Initializable {

    @FXML
    TextField titleTextBox;
    @FXML
    TextArea descriptionTextBox;
    @FXML
    TextField locationTextBox;
    @ FXML
    ComboBox<String> contactComboBox;
    @ FXML
    TextField typeTextBox;
    @ FXML
    ComboBox<Integer> customerComboBox;
    @ FXML
    ComboBox<Integer> userComboBox;
    @ FXML
    Label timeZoneLabel;
    @ FXML
    DatePicker apptDatePicker;
    @ FXML
    TextField startTimeTextBox;
    @ FXML
    TextField endTimeTextBox;
    @ FXML
    Button saveButton;
    @ FXML
    Button clearButton;
    @ FXML
    Button backButton;

    /*
     * @param event Button Press
     * @param switchPath path to new screen
     */
    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Executes additional inputs in the database
     */
    public void pressSaveButton(ActionEvent event) throws SQLException, IOException {


        Boolean validStartDateTime = true;
        Boolean validEndDateTime = true;
        Boolean validOverlap = true;
        Boolean validBusinessHours = true;
        String errorMessage = "";


        String title = titleTextBox.getText();
        String description = descriptionTextBox.getText();
        String location = locationTextBox.getText();
        String contactName = contactComboBox.getValue();
        String type = typeTextBox.getText();
        Integer customerID = customerComboBox.getValue();
        Integer userID = userComboBox.getValue();
        LocalDate apptDate = apptDatePicker.getValue();
        LocalDateTime endDateTime = null;
        LocalDateTime startDateTime = null;
        ZonedDateTime zonedEndDateTime = null;
        ZonedDateTime zonedStartDateTime = null;

        Integer contactID = ContactDB.findContactID(contactName);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            startDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(startTimeTextBox.getText(), formatter));
            validStartDateTime = true;
        }
        catch(DateTimeParseException error) {
            validStartDateTime = false;
            errorMessage += "Invalid Start time. Please ensure proper format HH:MM, including leading 0's.\n";
        }

        try {
            endDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(endTimeTextBox.getText(), formatter));
            validEndDateTime = true;
        }
        catch(DateTimeParseException error) {
            validEndDateTime = false;
            errorMessage += "Invalid End time. Please ensure proper format HH:MM, including leading 0's.\n";
        }

        // INPUT VALIDATION: Ensure all fields have been entered correctly
        if (title.isBlank() || description.isBlank() || location.isBlank() || contactName == null || type.isBlank() ||
                customerID == null || userID == null || apptDate == null || endDateTime == null ||
                startDateTime == null) {

            errorMessage += "Please ensure a value has been entered in all fields.\n";
            // Throws error if found
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidInput.showAndWait();
            return;

        }

        // INPUT VALIDATION: validates business hours if valid.
        validBusinessHours = validateBusinessHours(startDateTime, endDateTime, apptDate);
        validOverlap = validateCustomerOverlap(customerID, startDateTime, endDateTime, apptDate);

        if (!validBusinessHours) {
            errorMessage += "You are not allowed to schedule an appointment out of business hours. It should be between 8am to 10pm EST\n";
        }
        if (!validOverlap) {
            errorMessage += "Invalid, Customer Overlap. You cannot double book customers.\n";
        }

        System.out.println(errorMessage); // TODO - logger

        if (!validOverlap || !validBusinessHours || !validEndDateTime || !validStartDateTime) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidInput.showAndWait();
            return;

        }
        else {

            zonedStartDateTime = ZonedDateTime.of(startDateTime, LogonSession.getUserTimeZone());
            zonedEndDateTime = ZonedDateTime.of(endDateTime, LogonSession.getUserTimeZone());
            String loggedOnUserName = LogonSession.getLoggedOnUser().getUserName();

            // Convert to UTC
            zonedStartDateTime = zonedStartDateTime.withZoneSameInstant(ZoneOffset.UTC);
            zonedEndDateTime = zonedEndDateTime.withZoneSameInstant(ZoneOffset.UTC);

            // Add appointment to Database by completing all the fields
            Boolean success = AppointmentDB.addAppointment(title, description, location, type, zonedStartDateTime,
                    zonedEndDateTime, loggedOnUserName, loggedOnUserName, customerID, userID, contactID );

            // return message "successfully added" if found no errors
            if (success) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment added successfully!", clickOkay);
                alert.showAndWait();
                switchScreen(event, "/view_controller/AppointmentScene.fxml");
            }
            else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "failed to add appointment", clickOkay);
                alert.showAndWait();
            }

        }

    }

    public void pressClearButton() {
        titleTextBox.clear();
        descriptionTextBox.clear();
        locationTextBox.clear();
        typeTextBox.clear();
        startTimeTextBox.clear();
        endTimeTextBox.clear();
        contactComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        userComboBox.getSelectionModel().clearSelection();
        apptDatePicker.getEditor().clear();


    }

    /**
     * Returns back to previous scene
     *
     * @param event button press
     * @throws IOException
     */
    public void pressBackButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AppointmentScene.fxml");

    }

    /**
     * Accepts only appointments made within business hours
     *
     * @param startDateTime appointment datetime is started
     * @param endDateTime  appointment datetime ended
     * @param apptDate appointment date
     * @return returns Boolean showing valid input
     */
    public Boolean validateBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate apptDate) {
        // (8am to 10pm EST, Not including weekends)

        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, LogonSession.getUserTimeZone());
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, LogonSession.getUserTimeZone());

        ZonedDateTime startBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(8,00),
                ZoneId.of("America/New_York"));
        ZonedDateTime endBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(22, 00),
                ZoneId.of("America/New_York"));

        // Validates all the business hours and update time accordingly

        if (startZonedDateTime.isBefore(startBusinessHours) | startZonedDateTime.isAfter(endBusinessHours) |
                endZonedDateTime.isBefore(startBusinessHours) | endZonedDateTime.isAfter(endBusinessHours) |
                startZonedDateTime.isAfter(endZonedDateTime)) {
            return false;

        }
        else {
            return true;
        }

    }

    /**
     * Ensure no overlapping appointments are accepted
     *
     * @param inputCustomerID appointment of customer ID
     * @param startDateTime appointment start datetime
     * @param endDateTime appointment end datetime
     * @param apptDate appointment date

     * @return returns Boolean displaying valid input inserted
     */
    public Boolean validateCustomerOverlap(Integer inputCustomerID, LocalDateTime startDateTime,
                                           LocalDateTime endDateTime, LocalDate apptDate) throws SQLException {

        // Get list of appointments that might have conflicts
        ObservableList<Appointment> possibleConflicts = AppointmentDB.getCustomerFilteredAppointments(apptDate,
                inputCustomerID);
        // Removes any conflicting appointments with the existing one

        if (possibleConflicts.isEmpty()) {
            return true;
        }
        else {
            for (Appointment conflictAppt : possibleConflicts) {

                LocalDateTime conflictStart = conflictAppt.getStartDateTime().toLocalDateTime();
                LocalDateTime conflictEnd = conflictAppt.getEndDateTime().toLocalDateTime();

                if( conflictStart.isBefore(startDateTime) & conflictEnd.isAfter(endDateTime)) {
                    return false;
                }
                if (conflictStart.isBefore(endDateTime) & conflictStart.isAfter(startDateTime)) {
                    return false;
                }
                if (conflictEnd.isBefore(endDateTime) & conflictEnd.isAfter(startDateTime)) {
                    return false;
                }
                else {
                    return true;
                }

            }
        }
        return true;

    }

    /**
     * This Lambda expression - All the past dates are bared to be selected
     * @param location Scene path
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        timeZoneLabel.setText("Your Time Zone:" + LogonSession.getUserTimeZone());

        //Lambda Expression

        apptDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate apptDatePicker, boolean empty) {
                super.updateItem(apptDatePicker, empty);
                setDisable(
                        empty || apptDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                apptDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY || apptDatePicker.isBefore(LocalDate.now()));
            }
        });

        // Populate ComboBoxes
        try {
            customerComboBox.setItems(CustomerDB.getAllCustomerID());
            userComboBox.setItems(UserDB.getAllUserID());
            contactComboBox.setItems(ContactDB.getAllContactName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
