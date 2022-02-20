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
 * AddAppointment Class: Manages new appointment additions
 *
 * @author Hussein Coulibaly
 */
public class AddAppointment implements Initializable {

    @FXML TextField titleTextField;
    @FXML TextArea descriptionTextField;
    @FXML TextField locationTextField;
    @FXML ComboBox<String> contactComboBox;
    @FXML TextField typeTextField;
    @FXML ComboBox<Integer> customerComboBox;
    @FXML ComboBox<Integer> userComboBox;
    @FXML Label timeZoneTag;
    @FXML DatePicker apptDatePicker;
    @FXML TextField startTimeTextBox;
    @FXML TextField endTimeTextBox;
    @FXML Button saveButton;
    @FXML Button clearButton;
    @FXML Button backButton;


    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void saveButtonHandler(ActionEvent event) throws SQLException, IOException {


        Boolean validStartDateTime = true;
        Boolean validEndDateTime= true;
        Boolean validOverlap = true;
        Boolean validBusinessHours = true;
        String errorMessage = "";


        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String contactName = contactComboBox.getValue();
        String type = typeTextField.getText();
        Integer customerID = customerComboBox.getValue();
        Integer userID = userComboBox.getValue();
        LocalDate apptDate = apptDatePicker.getValue();
        LocalDateTime endDateTime = null;
        LocalDateTime startDateTime = null;
        ZonedDateTime zonedEndDateTime;
        ZonedDateTime zonedStartDateTime;

        int contactID = ContactDB.obtainContactID(contactName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            startDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(startTimeTextBox.getText(), formatter));
            validStartDateTime = true;
        }
        catch(DateTimeParseException error) {
            validStartDateTime = false;
            errorMessage += "Incorrect Appointment Start time. Please ensure proper format HH:MM is used.\n";
        }

        try {
            endDateTime = LocalDateTime.of(apptDatePicker.getValue(),
                    LocalTime.parse(endTimeTextBox.getText(), formatter));
            validEndDateTime = true;
        }
        catch(DateTimeParseException error) {
            validEndDateTime = false;
            errorMessage += "Incorrect Appointment End time. Please ensure proper format HH:MM is used.\n";
        }


        if (title.isBlank() || description.isBlank() || location.isBlank() || contactName == null || type.isBlank() ||
                customerID == null || userID == null || apptDate == null || endDateTime == null ||
                startDateTime == null) {

            errorMessage += "Please ensure all fields are completed.\n";

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidIn = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidIn.showAndWait();
            return;
        }

        validBusinessHours = validateBusinessHours(startDateTime, endDateTime, apptDate);
        validOverlap = checkOverlappedCustomer(customerID, startDateTime, endDateTime, apptDate);

        if (!validBusinessHours) {
            errorMessage += "You are not allowed to schedule an appointment out of business hours.(8am to 10pm EST)\n";
        }
        if (!validOverlap) {
            errorMessage += "Incorrect, Overlapped customer. You customer cannot be duplicated.\n";
        }

        System.out.println(errorMessage); // TODO - logger

        if (!validOverlap || !validBusinessHours || !validEndDateTime == false || !validStartDateTime == true) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidIn = new Alert(Alert.AlertType.WARNING, errorMessage, clickOkay);
            invalidIn.showAndWait();

        }
        else {

            zonedStartDateTime = ZonedDateTime.of(startDateTime, LoginSession.getUserTimeZone());
            zonedEndDateTime = ZonedDateTime.of(endDateTime, LoginSession.getUserTimeZone());
            String loggedOnUserName = LoginSession.getLoginUser().getUserName();

            // Convert to UTC
            zonedStartDateTime = zonedStartDateTime.withZoneSameInstant(ZoneOffset.UTC);
            zonedEndDateTime = zonedEndDateTime.withZoneSameInstant(ZoneOffset.UTC);


            Boolean success = AppointmentDB.addAppointment(title, description, location, type, zonedStartDateTime,
                    zonedEndDateTime, loggedOnUserName, loggedOnUserName, customerID, userID, contactID );


            if (success) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment added successfully!", clickOkay);
                alert.showAndWait();
                switchScreen(event, "/view_controller/AppointmentScene.fxml");
            }
            else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to add appointment", clickOkay);
                alert.showAndWait();
                return;
            }

        }

    }

    public void clearButtonHandler() {
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        startTimeTextBox.clear();
        endTimeTextBox.clear();
        contactComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        userComboBox.getSelectionModel().clearSelection();
        apptDatePicker.getEditor().clear();


    }


    public void backButtonHandler(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AppointmentScene.fxml");

    }


    public Boolean validateBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate apptDate) {

        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, LoginSession.getUserTimeZone());
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, LoginSession.getUserTimeZone());

        ZonedDateTime startBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(8,00),
                ZoneId.of("America/New_York"));
        ZonedDateTime endBusinessHours = ZonedDateTime.of(apptDate, LocalTime.of(22, 00),
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


    public Boolean checkOverlappedCustomer(Integer inputCustomerID, LocalDateTime startDateTime,
                                           LocalDateTime endDateTime, LocalDate apptDate) throws SQLException {


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


    public void initialize(URL location, ResourceBundle resources) {

        timeZoneTag.setText("Your Time Zone is:" + LoginSession.getUserTimeZone());

        //Lambda Expression

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

        try {
            customerComboBox.setItems(CustomerDB.getAllCustomerID());
            userComboBox.setItems(UserDB.getAllUserID());
            contactComboBox.setItems(ContactDB.getAllContactByName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
