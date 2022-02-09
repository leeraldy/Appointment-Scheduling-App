package view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDB;
import model.LogonSession;
import utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * * @author Hussein Coulibaly
 *
 * Handles the main screen and other screens
 */
public class AppointmentScene implements Initializable {
    @FXML
    Button newAppointmentButton;
    @FXML
    Button editAppointmentButton;
    @FXML
    Button deleteButton;
    @FXML
    Button customersViewButton;
    @FXML
    Button reportsButton;
    @FXML
    Button logOutButton;
    @FXML
    Button nextTimeButton;
    @FXML
    Button previousTimeButton;
    @FXML
    RadioButton monthFilterButton;
    @FXML
    RadioButton weekFilterButton;
    @FXML
    RadioButton noFilterButton;
    @FXML
    TableView<Appointment> appointmentTable;
    @FXML
    TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    TableColumn<Appointment, String> titleColumn;
    @FXML
    TableColumn<Appointment, String> descriptionColumn;
    @FXML
    TableColumn<Appointment, String> locationColumn;
    @FXML
    TableColumn<Appointment, String> contactColumn;
    @FXML
    TableColumn<Appointment, String> typeColumn;
    @FXML
    TableColumn<Appointment, ZonedDateTime> startDateTimeColumn;
    @FXML
    TableColumn<Appointment, ZonedDateTime> endDateTimeColumn;
    @FXML
    TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    ToggleGroup filterToggle;
    @FXML
    Label selectedTimeLabel;

    // Set date filter.
    ZonedDateTime startRangeMarker;
    ZonedDateTime endRangeMarker;

    /**
     * Loads new scene
     *
     * @param event button press
     * @param switchPath path of new scene
     */
    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Generates new toggle group blocking multiple selections
     */
    public void initToggleGroup() {

        filterToggle = new ToggleGroup();

        noFilterButton.setToggleGroup(filterToggle);
        weekFilterButton.setToggleGroup(filterToggle);
        monthFilterButton.setToggleGroup(filterToggle);

    }

    /**
     * Generates the list of all appointments on screen
     *
     * @param event Button Press
     */
    public void pressNoFilterButton(ActionEvent event) {
        monthFilterButton.setSelected(false);
        weekFilterButton.setSelected(false);

        ObservableList<Appointment> allAppts;
        try {
            allAppts = AppointmentDB.getAllAppointments();
        }
        catch (SQLException error){
            // Database reconnection if the network drops.
            error.printStackTrace();
            DBConnection.connectDB();
            try {
                allAppts = AppointmentDB.getAllAppointments();
            } catch (SQLException anotherError) {
                anotherError.printStackTrace();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "DB connection failed. Please restart", clickOkay);
                invalidInput.showAndWait();
                return;
            }

        }
        populateAppointments(allAppts);
        selectedTimeLabel.setText("All Appointments");
        startRangeMarker = null;


    }

    /**
     * Generates a list of all appointments to by weekly
     *
     * @param event Button Press
     * @throws SQLException
     */
    public void pressWeekFilterButton(ActionEvent event) throws SQLException {

        monthFilterButton.setSelected(false);
        noFilterButton.setSelected(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        startRangeMarker = ZonedDateTime.now(LogonSession.getUserTimeZone());
        endRangeMarker = startRangeMarker.plusWeeks(1);

        // Convert to UTC
        ZonedDateTime startRange = startRangeMarker.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endRange = endRangeMarker.withZoneSameInstant(ZoneOffset.UTC);

        // Search in Database for time frame
        filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);
        // Populate filtered appointments
        populateAppointments(filteredAppts);

        selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                LogonSession.getUserTimeZone());





    }

    /**
     * Generates a list of all appointments to by month
     *
     * @param event Button Press
     * @throws SQLException
     */
    public void pressMonthFilterButton(ActionEvent event) throws SQLException {
        weekFilterButton.setSelected(false);
        noFilterButton.setSelected(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        startRangeMarker = ZonedDateTime.now(LogonSession.getUserTimeZone());
        endRangeMarker = startRangeMarker.plusMonths(1);

        // Convert to UTC
        ZonedDateTime startRange = startRangeMarker.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endRange = endRangeMarker.withZoneSameInstant(ZoneOffset.UTC);

        filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);
        // Populate filtered appointments
        populateAppointments(filteredAppts);
        // update label
        selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                LogonSession.getUserTimeZone());



    }

    /**
     * Generates a list of filtered appointments
     *
     * @param event Button Click
     * @throws SQLException
     */
    public void pressNextButton(ActionEvent event) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();

        if (filterToggle.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startRange = startRangeMarker.plusWeeks(1);
            ZonedDateTime endRange = endRangeMarker.plusWeeks(1);

            // update markers
            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to UTC for the DB
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointments(filteredAppts);

            // update label
            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogonSession.getUserTimeZone());

        }
        if (filterToggle.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startRange = startRangeMarker.plusMonths(1);
            ZonedDateTime endRange = endRangeMarker.plusMonths(1);

            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to time UTC for the Databae
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointments(filteredAppts);

            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogonSession.getUserTimeZone());

        }

    }

    /**
     * @param event Button Press
     * @throws SQLException
     */
    public void pressBackButton(ActionEvent event) throws SQLException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();

        if (filterToggle.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startRange = startRangeMarker.minusWeeks(1);
            ZonedDateTime endRange = endRangeMarker.minusWeeks(1);

            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert time to UTC
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointments(filteredAppts);

            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogonSession.getUserTimeZone());

        }
        if (filterToggle.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startRange = startRangeMarker.minusMonths(1);
            ZonedDateTime endRange = endRangeMarker.minusMonths(1);

            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to time UTC
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointments(filteredAppts);

            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogonSession.getUserTimeZone());
        }

    }

    /**
     * Deletes existing appointment and returns appointments table
     *
     * @param event Button Press
     * @throws IOException
     * @throws SQLException
     */
    public void pressDeleteButton(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "No selected Appointment", clickOkay);
            invalidInput.showAndWait();
            return;
        }
        else {
            // show alert and ensure user wants to delete
            ButtonType clickYes = ButtonType.YES;
            ButtonType clickNo = ButtonType.NO;
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete Appointment: "
                    + selectedAppt.getAppointmentID() + " ?", clickYes, clickNo);
            Optional<ButtonType> result = deleteAlert.showAndWait();

            // Appointment deleted upon user confirmation
            if (result.get() == ButtonType.YES) {
                Boolean success = AppointmentDB.deleteAppointment(selectedAppt.getAppointmentID());

                if (success) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deletedAppt = new Alert(Alert.AlertType.CONFIRMATION, "Appointment " + selectedAppt.getAppointmentID() + ": " + selectedAppt.getType() + " deleted successfully!", clickOkay);
                    deletedAppt.showAndWait();

                }
                else {

                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Failed to delete Appointment", clickOkay);
                    deleteAppt.showAndWait();

                }

                // Return appointment screen
                try {
                    populateAppointments(AppointmentDB.getAllAppointments());
                }
                catch (SQLException error){
                    //TODO - log error
                    error.printStackTrace();
                }

            }
            else {
                return;
            }
        }
    }

    /**
     * Redirects add screen appointment
     *
     * @param event Button Click
     * @throws IOException
     */
    public void pressNewButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AddAppointment.fxml");

    }

    /**
     * Exits user out from the program
     *
     * @param event Button Press
     * @throws IOException
     */
    public void pressLogoutButton(ActionEvent event) throws IOException {
        ButtonType clickYes = ButtonType.YES;
        ButtonType clickNo = ButtonType.NO;
        Alert logOff = new Alert(Alert.AlertType.WARNING, "Are you sure you want to Log Off?", clickYes, clickNo);
        Optional<ButtonType> result = logOff.showAndWait();

        if (result.get() == ButtonType.YES) {
            LogonSession.logOff();
            switchScreen(event, "/view_controller/Login.fxml");
        }
        else {
            return;
        }


    }

    /**
     * Redirects to another screen
     *
     * @param event Button Click
     * @throws IOException
     * @throws SQLException
     */
    public void pressEditButton(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();
        // If no selection made, shows errors
        if (selectedAppt == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "No selected Appointment", clickOkay);
            invalidInput.showAndWait();
            return;

        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/ModifyAppointment.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        ModifyAppointment controller = loader.getController();
        controller.initData(selectedAppt);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);

    }

    /**
     * Generates customer screen
     *
     * @param event Button Press
     * @throws IOException
     */
    public void pressCustomerButton(ActionEvent event) throws IOException {

        switchScreen(event, "/view_controller/CustomerScene.fxml");

    }

    /**
     * Generates reports screen
     *
     * @param event Button Press
     * @throws IOException
     */
    public void pressReportsPage(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/Reports.fxml");

    }

    /**
     * Populates appointments on the screen
     *
     * @param inputList list of appointments
     */
    public void populateAppointments(ObservableList<Appointment> inputList) {

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, ZonedDateTime>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerID"));
        appointmentTable.setItems(inputList);

    }

    /**
     * Displays any cancelled appointmemt
     *
     * @param inputList list of all appointments
     */
    public void checkCanceled(ObservableList<Appointment> inputList) {

        inputList.forEach((appt) -> {
            if (appt.getType().equalsIgnoreCase("canceled")) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Appointment " + appt.getAppointmentID() +
                        " is canceled.", clickOkay);
                invalidInput.showAndWait();
            }
        });

    }


    /**
     * Initializes mainscreen
     *
     * @param location location / time zone
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)   {


        noFilterButton.setSelected(true);
        initToggleGroup();


        ObservableList<Appointment> allAppts = null;
        try {
            allAppts = AppointmentDB.getAllAppointments();
        }
        catch (SQLException error){
            // Database reconnection if connection is lost
            error.printStackTrace();
            DBConnection.connectDB();
            try {
                allAppts = AppointmentDB.getAllAppointments();
            } catch (SQLException anotherError) {
                anotherError.printStackTrace();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "DB connection failed. please restart", clickOkay);
                invalidInput.showAndWait();
                return;
            }

        }
        populateAppointments(allAppts);
        checkCanceled(allAppts);


    }

}