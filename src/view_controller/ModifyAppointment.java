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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * ModifyAppointment Class: Manages any changes of the saved appointments
 * @author Hussein Coulibaly
 */
public class ModifyAppointment implements Initializable {

    @FXML
    TextField appointmentIDTextField;
    @FXML
    TextField titleTextField;
    @FXML
    TextArea descriptionTextField;
    @FXML
    TextField locationTextField;
    @FXML
    ComboBox<String> contactComboBox;
    @FXML
    TextField typeTextField;
    @FXML
    ComboBox<Integer> customerComboBox;
    @FXML
    ComboBox<Integer> userComboBox;
    @FXML
    DatePicker apptDatePicker;
    @FXML
    TextField startTimeTextField;
    @FXML
    TextField endTimeTextField;
    @FXML
    Button saveButton;
    @FXML
    Button clearButton;
    @FXML
    Button backButton;
    @FXML
    Label timeZoneLabel;



    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void MetaData(Appointment selectedAppt) throws SQLException {

        try {
            LocalDate apptDate = selectedAppt.getStartDateTime().toLocalDateTime().toLocalDate();
        }
        catch (NullPointerException error) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "No Date is selected", clickOkay);
            invalidInput.showAndWait();
            return;
        }
        ZonedDateTime startDateTimeUTC = selectedAppt.getStartDateTime().toInstant().atZone(ZoneOffset.UTC);
        ZonedDateTime endDateTimeUTC = selectedAppt.getEndDateTime().toInstant().atZone(ZoneOffset.UTC);

        ZonedDateTime localStartDateTime = startDateTimeUTC.withZoneSameInstant(LoginSession.getUserTimeZone());
        ZonedDateTime localEndDateTime = endDateTimeUTC.withZoneSameInstant(LoginSession.getUserTimeZone());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String localStartString = localStartDateTime.format(formatter);
        String localEndString = localEndDateTime.format(formatter);


        appointmentIDTextField.setText(selectedAppt.getAppointmentID().toString());
        titleTextField.setText(selectedAppt.getTitle());
        descriptionTextField.setText(selectedAppt.getDescription());
        locationTextField.setText(selectedAppt.getLocation());
        contactComboBox.setItems(ContactDB.getAllContactByName());
        contactComboBox.getSelectionModel().select(selectedAppt.getContactName());
        typeTextField.setText(selectedAppt.getType());
        customerComboBox.setItems(CustomerDB.getAllCustomerID());
        customerComboBox.getSelectionModel().select(selectedAppt.getCustomerID());
        userComboBox.setItems(UserDB.getAllUserID());
        userComboBox.getSelectionModel().select(selectedAppt.getUserID());
        apptDatePicker.setValue(selectedAppt.getStartDateTime().toLocalDateTime().toLocalDate());
        startTimeTextField.setText(localStartString);
        endTimeTextField.setText(localEndString);


    }


    public Boolean validateBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate apptDate) {


        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, LoginSession.getUserTimeZone());
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, LoginSession.getUserTimeZone());

        ZonedDateTime startBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(8,0),
                ZoneId.of("America/New_York"));
        ZonedDateTime endBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(22, 0),
                ZoneId.of("America/New_York"));


        if (startZonedDateTime.isBefore(startBusinessHours) | startZonedDateTime.isAfter(endBusinessHours) |
                endZonedDateTime.isBefore(startBusinessHours) | endZonedDateTime.isAfter(endBusinessHours) |
                startZonedDateTime.isAfter(endZonedDateTime)) {
            return false;

        }
        else {
            return true;
        }

    }


    public Boolean validateCustomerOverlap(Integer inputCustomerID, LocalDateTime startDateTime,
                                           LocalDateTime endDateTime, LocalDate apptDate) throws SQLException {

        // Check for any appointment conflicts
        ObservableList<Appointment> possibleConflicts = AppointmentDB.getAppointmentsFilteredByCustomer(apptDate,
                inputCustomerID);

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

    public void clearButtonHandler(ActionEvent event) {
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        startTimeTextField.clear();
        endTimeTextField.clear();
        contactComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        userComboBox.getSelectionModel().clearSelection();
        apptDatePicker.getEditor().clear();

    }


    public void backButtonHandler(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AppointmentScene.fxml");

    }


    public void saveButtonHandler(ActionEvent event) throws SQLException, IOException {

        Boolean validStartDateTime;
        Boolean validEndDateTime ;
        Boolean validOverlap;
        Boolean validBusinessHours;
        String errorMessage = "";

        Integer apptID = Integer.parseInt(appointmentIDTextField.getText());
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String contactName = contactComboBox.getValue();
        String type = typeTextField.getText();
        Integer customerID = customerComboBox.getValue();
        Integer userID = userComboBox.getValue();
        LocalDate apptDate = apptDatePicker.getValue();

        // Initializes Local Zone time
        LocalDateTime endDateTime = null;
        LocalDateTime startDateTime = null;
        ZonedDateTime zonedEndDateTime = null;
        ZonedDateTime zonedStartDateTime = null;

        Integer contactID = ContactDB.obtainContactID(contactName);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");



        try {
            startDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(startTimeTextField.getText(), formatter));
            validStartDateTime = true;
        }
        catch(DateTimeParseException error) {
            validStartDateTime = false;
            errorMessage += "Incorrect Appointment Start time. Please ensure correct format HH:MM is used including leading 0's.\n";
        }

        try {
            endDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(endTimeTextField.getText(), formatter));
            validEndDateTime = true;
        }
        catch(DateTimeParseException error) {
            validEndDateTime = false;
            errorMessage += "Incorrect Appointment End time. Please ensure correct format HH:MM, including leading 0's.\n";
        }


        if (title.isBlank() || description.isBlank() || location.isBlank() || contactName == null || type.isBlank() ||
                customerID == null || userID == null || apptDate == null || endDateTime == null ||
                startDateTime == null) {

            errorMessage += "Please ensure there's no empty field.\n";

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidInput.showAndWait();
            return;

        }


        validBusinessHours = validateBusinessHours(startDateTime, endDateTime, apptDate);
        validOverlap = validateCustomerOverlap(customerID, startDateTime, endDateTime, apptDate);

        // INPUT VALIDATION: Displays corresponding error for user
        if (!validBusinessHours) {
            errorMessage += "Incorrect, Appointment should be between Business Hours.(8am to 10pm EST)\n";
        }
        if (!validOverlap) {
            errorMessage += "Incorrect Overlapped Customer. Customers Appointment cannot be Duplicated.\n";
        }


        // INPUT VALIDATION
        if (!validOverlap || !validBusinessHours || !validEndDateTime || !validStartDateTime) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidIn = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidIn.showAndWait();

        }
        else {

            zonedStartDateTime = ZonedDateTime.of(startDateTime, LoginSession.getUserTimeZone());
            zonedEndDateTime = ZonedDateTime.of(endDateTime, LoginSession.getUserTimeZone());
            String loggedOnUserName = LoginSession.getLoginUser().getUserName();


            zonedStartDateTime = zonedStartDateTime.withZoneSameInstant(ZoneOffset.UTC);
            zonedEndDateTime = zonedEndDateTime.withZoneSameInstant(ZoneOffset.UTC);

            // Add appointments
            Boolean success = AppointmentDB.updateAppointment(apptID, title, description, location, type, zonedStartDateTime,
                    zonedEndDateTime, loggedOnUserName, customerID, userID, contactID );

            if (success) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidIn = new Alert(Alert.AlertType.CONFIRMATION, "Appointment updated successfully!", clickOkay);
                invalidIn.showAndWait();
                switchScreen(event, "/view_controller/AppointmentScene.fxml");
            }
            else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Unable to Update appointment", clickOkay);
                invalidInput.showAndWait();
            }

        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        timeZoneLabel.setText(LoginSession.getUserTimeZone().toString());

    }
}
