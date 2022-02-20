package view_controller;


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
import model.LoginSession;
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
 * AppointmentScene Class: Manages the main screen and other screens objects
 */
public class AppointmentScene implements Initializable {

    @FXML Button newAppointmentButton;
    @FXML Button editAppointmentButton;
    @FXML Button deleteButton;
    @FXML Button customersViewButton;
    @FXML Button reportsButton;
    @FXML Button logOutButton;
    @FXML Button nextTimeButton;
    @FXML Button previousTimeButton;
    @FXML RadioButton monthFilterButton;
    @FXML RadioButton weekFilterButton;
    @FXML RadioButton noFilterButton;
    @FXML TableView<Appointment> appointmentTable;
    @FXML TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML TableColumn<Appointment, String> titleColumn;
    @FXML TableColumn<Appointment, String> descriptionColumn;
    @FXML TableColumn<Appointment, String> locationColumn;
    @FXML TableColumn<Appointment, String> contactColumn;
    @FXML TableColumn<Appointment, String> typeColumn;
    @FXML TableColumn<Appointment, ZonedDateTime> startDateTimeColumn;
    @FXML TableColumn<Appointment, ZonedDateTime> endDateTimeColumn;
    @FXML TableColumn<Appointment, Integer> customerIdColumn;
    @FXML ToggleGroup filterToggleView;
    @FXML Label selectedTimeLabel;


    ZonedDateTime startSpanToken;
    ZonedDateTime endSpanToken;


    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void initToggleGroup() {

        filterToggleView = new ToggleGroup();

        noFilterButton.setToggleGroup(filterToggleView);
        weekFilterButton.setToggleGroup(filterToggleView);
        monthFilterButton.setToggleGroup(filterToggleView);

    }


    public void noFilterButtonHandler(ActionEvent event) {
        monthFilterButton.setSelected(false);
        weekFilterButton.setSelected(false);

        ObservableList<Appointment> allAppts;
        try {
            allAppts = AppointmentDB.getAllAppointments();
        }
        catch (SQLException error){

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
        populateAppointmentsView(allAppts);
        selectedTimeLabel.setText("All Appointments");
        startSpanToken = null;


    }


    public void weekFilterButtonHandler(ActionEvent event) throws SQLException {

        monthFilterButton.setSelected(false);
        noFilterButton.setSelected(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts;
        startSpanToken = ZonedDateTime.now(LoginSession.getUserTimeZone());
        endSpanToken = startSpanToken.plusWeeks(1);

        // UTC Time conversion
        ZonedDateTime startSpan = startSpanToken.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endSpan = endSpanToken.withZoneSameInstant(ZoneOffset.UTC);


        filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

        populateAppointmentsView(filteredAppts);

        selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                LoginSession.getUserTimeZone());





    }


    public void monthFilterButtonHandler(ActionEvent event) throws SQLException {
        weekFilterButton.setSelected(false);
        noFilterButton.setSelected(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts;
        startSpanToken = ZonedDateTime.now(LoginSession.getUserTimeZone());
        endSpanToken = startSpanToken.plusMonths(1);

        // UTC time conversion
        ZonedDateTime startSpan = startSpanToken.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endSpan = endSpanToken.withZoneSameInstant(ZoneOffset.UTC);

        filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

        populateAppointmentsView(filteredAppts);
        selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                LoginSession.getUserTimeZone());



    }


    public void nextButtonHandler(ActionEvent event) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts;

        if (filterToggleView.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startSpan = startSpanToken.plusWeeks(1);
            ZonedDateTime endSpan = endSpanToken.plusWeeks(1);


            startSpanToken = startSpan;
            endSpanToken = endSpan;


            startSpan = startSpan.withZoneSameInstant(ZoneOffset.UTC);
            endSpan = endSpan.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

            populateAppointmentsView(filteredAppts);


            selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                    LoginSession.getUserTimeZone());

        }
        if (filterToggleView.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startSpan = startSpanToken.plusMonths(1);
            ZonedDateTime endSpan = endSpanToken.plusMonths(1);

            startSpanToken = startSpan;
            endSpanToken = endSpan;

            startSpan = startSpan.withZoneSameInstant(ZoneOffset.UTC);
            endSpan = endSpan.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

            populateAppointmentsView(filteredAppts);

            selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                    LoginSession.getUserTimeZone());

        }

    }


    public void backButtonHandler(ActionEvent event) throws SQLException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts;

        if (filterToggleView.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startSpan = startSpanToken.minusWeeks(1);
            ZonedDateTime endSpan = endSpanToken.minusWeeks(1);

            startSpanToken = startSpan;
            endSpanToken = endSpan;

            //  UTC Time Conversion
            startSpan = startSpan.withZoneSameInstant(ZoneOffset.UTC);
            endSpan = endSpan.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

            populateAppointmentsView(filteredAppts);

            selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                    LoginSession.getUserTimeZone());

        }
        if (filterToggleView.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startSpan = startSpanToken.minusMonths(1);
            ZonedDateTime endSpan = endSpanToken.minusMonths(1);

            startSpanToken = startSpan;
            endSpanToken = endSpan;


            startSpan = startSpan.withZoneSameInstant(ZoneOffset.UTC);
            endSpan = endSpan.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getAllDateFilteredAppointmentsView(startSpan, endSpan);

            populateAppointmentsView(filteredAppts);

            selectedTimeLabel.setText(startSpanToken.format(formatter) + " - " + endSpanToken.format(formatter) + " " +
                    LoginSession.getUserTimeZone());
        }

    }

    // Deletes existing appointments
    public void deleteButtonHandler(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidIn = new Alert(Alert.AlertType.WARNING, "No Appointment is selected", clickOkay);
            invalidIn.showAndWait();
        }
        else {

            ButtonType clickYes = ButtonType.YES;
            ButtonType clickNo = ButtonType.NO;
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete Appointment: "
                    + selectedAppt.getAppointmentID() + " ?", clickYes, clickNo);
            Optional<ButtonType> result = deleteAlert.showAndWait();

            // Appointment deleted upon user confirmation
            if (result.get() == ButtonType.YES) {
                Boolean success = AppointmentDB.removeAppointment(selectedAppt.getAppointmentID());

                if (success) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deletedAppt = new Alert(Alert.AlertType.CONFIRMATION, "Appointment " + selectedAppt.getAppointmentID() + ": " + selectedAppt.getType() + " deleted successfully!", clickOkay);
                    deletedAppt.showAndWait();

                }
                else {

                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Unable to delete Appointment", clickOkay);
                    deleteAppt.showAndWait();

                }


                try {
                    populateAppointmentsView(AppointmentDB.getAllAppointments());
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

    public void newButtonHandler(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AddAppointment.fxml");

    }


    public void logoutButtonHandler(ActionEvent event) throws IOException {
        ButtonType clickYes = ButtonType.YES;
        ButtonType clickNo = ButtonType.NO;
        Alert logOff = new Alert(Alert.AlertType.WARNING, "Are you sure you want to Sign Off?", clickYes, clickNo);
        Optional<ButtonType> result = logOff.showAndWait();

        if (result.get() == ButtonType.YES) {
            LoginSession.SignOff();
            switchScreen(event, "/view_controller/Login.fxml");
        }
        else {
            return;
        }


    }


    public void editButtonHandler(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "No Appointment is selected", clickOkay);
            invalidInput.showAndWait();

        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/ModifyAppointment.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        ModifyAppointment controller = loader.getController();
        controller.MetaData(selectedAppt);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);

    }

    // Returns Customer Screen

    public void customerButtonHandler(ActionEvent event) throws IOException {

        switchScreen(event, "/view_controller/CustomerScene.fxml");

    }

    public void reportHandler(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/Reports.fxml");

    }


    public void populateAppointmentsView(ObservableList<Appointment> inputList) {

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentTable.setItems(inputList);

    }


    public void auditCanceledAppointment(ObservableList<Appointment> inputList) {

        inputList.forEach((appt) -> {
            if (appt.getType().equalsIgnoreCase("canceled")) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Appointment " + appt.getAppointmentID() +
                        " is canceled.", clickOkay);
                invalidInput.showAndWait();
            }
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources)   {


        noFilterButton.setSelected(true);
        initToggleGroup();


        ObservableList<Appointment> allAppts = null;
        try {
            allAppts = AppointmentDB.getAllAppointments();
        }
        catch (SQLException error){

            error.printStackTrace();
            DBConnection.connectDB();
            try {
                allAppts = AppointmentDB.getAllAppointments();
            } catch (SQLException anotherError) {
                anotherError.printStackTrace();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidIn = new Alert(Alert.AlertType.WARNING, "Database connection failed. Please try to restart", clickOkay);
                invalidIn.showAndWait();
            }

        }
        populateAppointmentsView(allAppts);
        auditCanceledAppointment(allAppts);


    }

}