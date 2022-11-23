package controller;

import utils.*;
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
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * EditAppointment Class: This class handles all editable appointment objects
 * @author Hussein Coulibaly
 */



public class EditAppointment implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField apptIDTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private TextField typeTextField;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private DatePicker apptDatePicker;
    @FXML private TextField customerID;
    @FXML private ComboBox<User> userIDComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    /**
     * This method cancels an editing appointment action
     *
     * @param event by clicking cancel button
     * @throws IOException returns an error if found
     */
    
    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Cancel action?");
        alert.setContentText("Do you want to cancel updating the appointment?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/AppointmentViewer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method saves an edited appointment in the DB
     * @param event by clicking save button appointment is saved
     * @throws IOException returns an error if one is found
     */
    
    @FXML
    public void saveButtonHandler(ActionEvent event) throws IOException {

            int apptID = Integer.parseInt(apptIDTextField.getText());
            String customerId = customerID.getText();
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            Contact contact = contactComboBox.getValue();
            String type = typeTextField.getText();
            LocalDate apptDate = apptDatePicker.getValue();
            LocalTime startDateTime = startTimeComboBox.getValue();
            LocalTime endDateTime = endTimeComboBox.getValue();
            User userID = userIDComboBox.getValue();
            

            if (title.isBlank() || description.isBlank() || location.isBlank() || contact == null || type.isBlank() || apptDate == null || startDateTime == null || endDateTime == null || customerId.isBlank() || userID == null) {

                Alert editAlert = new Alert(Alert.AlertType.WARNING);
                editAlert.setHeaderText("Missing Fields");
                editAlert.setContentText("Please all fields are required!");
                editAlert.showAndWait();

            } else {

                Timestamp startTime = Timestamp.valueOf(LocalDateTime.of( apptDate, startTimeComboBox.getValue()));
                Timestamp endTime = Timestamp.valueOf(LocalDateTime.of( apptDate, endTimeComboBox.getValue()));
                int custID = Integer.parseInt(customerId);

                if (LocalDateTime.of(apptDate, endTimeComboBox.getValue()).isAfter(LocalDateTime.of(apptDate, startTimeComboBox.getValue())))
                {
                    Appointment newAppointment = new Appointment(apptID, title, description, location, contact.getContactID(), contact.getContactName(), type, startTime, endTime, custID, userID.getUserID());

                    if (AppointmentDB.checkOverlappedAppointment(newAppointment))
                    {

                        Alert overlapAlert = new Alert(Alert.AlertType.WARNING);
                        overlapAlert.setHeaderText("Overlapping Appointment?");
                        overlapAlert.setContentText("Appointment overlaps with an existing appointment for the selected customer.");
                        overlapAlert.showAndWait();
                    }
                    else {

                        AppointmentDB.modifyAppointment(title, description, location, type, startTime, endTime, custID, userID.getUserID(), contact.getContactID(), apptID);

                        Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        successAlert.setHeaderText("Confirmation");
                        successAlert.setContentText("Appointment updated successfully!");
                        successAlert.showAndWait();

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("../view/AppointmentViewer.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                }
                else
                {
                    Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                    invalidAlert.setHeaderText("Invalid Time");
                    invalidAlert.setContentText("Appointment end time must be after appointment start time.");
                    invalidAlert.showAndWait();
                }

            }

        }

    /**
     * This method populates customer ID into the customer table
     * @param event by clicking the selected customer
     */

    @FXML
    public void populateCustomerData(MouseEvent event)
    {
        customerID.setText(String.valueOf(customerTable.getSelectionModel().getSelectedItem().getCustomerID()));
    }

    Appointment selectedAppointment;


    /**
     * This method populates the edited appointment in the appointment viewer
     * @param selectedAppointment generates the updated appointment
     */

    public void populateAppointment(Appointment selectedAppointment)
    {

        this.selectedAppointment = selectedAppointment;

        apptIDTextField.setText(Integer.toString(selectedAppointment.getApptID()));
        titleTextField.setText(selectedAppointment.getTitle());
        descriptionTextField.setText(selectedAppointment.getDescription());
        locationTextField.setText(selectedAppointment.getLocation());

        for (Contact contact : contactComboBox.getItems()) {
            if (selectedAppointment.contactID == contact.getContactID()) {
                contactComboBox.setValue(contact);
                break;
            }
        }

        typeTextField.setText(selectedAppointment.getType());
        LocalTime startTime = selectedAppointment.getStartTime().toLocalDateTime().toLocalTime();
        startTimeComboBox.setValue(startTime);
        LocalTime endTime = selectedAppointment.getEndTime().toLocalDateTime().toLocalTime();
        endTimeComboBox.setValue(endTime);
        LocalDate apptDate = selectedAppointment.getStartTime().toLocalDateTime().toLocalDate();
        apptDatePicker.setValue(apptDate);
        customerID.setText(String.valueOf(selectedAppointment.getCustomerID()));

        for (User user : userIDComboBox.getItems())
        {
            if (selectedAppointment.userID == user.getUserID())
            {
                userIDComboBox.setValue(user);
                break;
            }
        }
    }

    /**
     * This method initializes editAppointment screen
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

        LocalTime apptStartBusinessHours = LocalTime.of(8, 0);
        LocalDateTime validStartDateTime = LocalDateTime.of(LocalDate.now(), apptStartBusinessHours);
        ZonedDateTime startMinZonedDateTime = validStartDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startMinDateTime = startMinZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptStartTimeMinSpan = startMinDateTime.toLocalTime();

        LocalTime apptEndBusinessHours = LocalTime.of(21, 45);
        LocalDateTime validStartMaxDateTime = LocalDateTime.of(LocalDate.now(), apptEndBusinessHours);
        ZonedDateTime startMaxZonedDateTime = validStartMaxDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startMaxDateTime = startMaxZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptStartMaxTime = startMaxDateTime.toLocalTime();

        while (apptStartTimeMinSpan.isBefore(apptStartMaxTime.plusSeconds(1)))
        {
            startTimeComboBox.getItems().add(apptStartTimeMinSpan);
            apptStartTimeMinSpan = apptStartTimeMinSpan.plusMinutes(15);
        }

        LocalTime endBusinessHours = LocalTime.of(8, 15);
        LocalDateTime validEndDateTime = LocalDateTime.of(LocalDate.now(), endBusinessHours);
        ZonedDateTime startZonedEndTime = validEndDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endDateTime = startZonedEndTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptEndTimeMinSpan = endDateTime.toLocalTime();

        LocalTime appointmentEndTimeMaxEST = LocalTime.of(22, 0);
        LocalDateTime validEndMaxDateTime = LocalDateTime.of(LocalDate.now(), appointmentEndTimeMaxEST);
        ZonedDateTime endMaxZonedDateTime = validEndMaxDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endMaxDateTime = endMaxZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime apptEndMaxTime = endMaxDateTime.toLocalTime();

        while (apptEndTimeMinSpan.isBefore(apptEndMaxTime.plusSeconds(1)))
        {
            endTimeComboBox.getItems().add(apptEndTimeMinSpan);
            apptEndTimeMinSpan = apptEndTimeMinSpan.plusMinutes(15);
        }

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDB.getAllCustomers());
        contactComboBox.setItems(ContactDB.getAllContacts());
        userIDComboBox.setItems(UserDB.getAllUsers());
    }
}
