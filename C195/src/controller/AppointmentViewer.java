package controller;

import utils.AppointmentDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AppointmentViewer Class: This class handles appointment viewer screen
 * @author Hussein Coulibaly
 */



public class AppointmentViewer implements Initializable {
    Stage stage;
    Parent scene;


    @FXML private RadioButton weeklyFilterButton;
    @FXML private RadioButton monthlyFilterButton;
    @FXML private RadioButton noFilterButton;
    @FXML private ToggleGroup filterToggleGroup;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIDColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startDateTimeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endDateTimeColumn;
    @FXML private TableColumn<Appointment, Integer> customerIDColumn;


    /**
     * This method redirects the end user to the main console
     * @param event by clicking the return button
     * @throws IOException return an error if found
     */
    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainConsole.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * This method adds a new appointment to the DB
     * @param event by clicking new button
     * @throws IOException returns an error if found
     */

    @FXML
    public void newButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method edits existing appointment in the DB
     * @param event by clicking edit button
     * @throws IOException returns an error if found
     */

    @FXML
    public void editButtonHandler(ActionEvent event) throws IOException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {

            Alert editAlert = new Alert(Alert.AlertType.WARNING);
            editAlert.setHeaderText("Edit appointment?");
            editAlert.setContentText("No appointment was selected to update");

            Optional<ButtonType> result = editAlert.showAndWait();
        }

        else
        {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/EditAppointment.fxml"));
            loader.load();

            EditAppointment controller = loader.getController();
            controller.populateAppointment(appointmentTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method deletes an existing appointment saved in the DB
     * @param event by clicking delete button
     * @throws IOException return an error if found
     */

    @FXML
    public void deleteButtonHandler(ActionEvent event) throws IOException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {

            Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
            deleteAlert.setHeaderText("Edit appointment?");
            deleteAlert.setContentText("No appointment was selected to update");

            Optional<ButtonType> result = deleteAlert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete appointment?");
            alert.setContentText("Deleting this appointment is permanent and cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                int appointmentId = appointmentTable.getSelectionModel().getSelectedItem().getApptID();

                String selectedType = appointmentTable.getSelectionModel().getSelectedItem().getType();

                String selectedApptID = String.valueOf(appointmentTable.getSelectionModel().getSelectedItem().getApptID());

                AppointmentDB.deleteAppointment(appointmentId);

                appointmentTable.setItems(AppointmentDB.getAllAppointments());

                Alert delAlert = new Alert(Alert.AlertType.CONFIRMATION);
                delAlert.setHeaderText("Delete Appointment?");
                delAlert.setContentText("Appointment " + selectedApptID + " : " + selectedType + " deleted successfully");

                delAlert.showAndWait();
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Delete Appointment?");
                errorAlert.setContentText("The selected appointment was not deleted.");

                errorAlert.showAndWait();
            }
        }
    }

    /**
     * This method filters all the appointments in the DB and returns all of them
     * @param event by clicking no filter radio button
     */

    @FXML
    public void noFilterButton(ActionEvent event) {

        weeklyFilterButton.setSelected(false);
        monthlyFilterButton.setSelected(false);

        appointmentTable.setItems(AppointmentDB.getAllAppointments());
    }

    /**
     * This method generates appointment sorted by weekly in the DB
     * @param event by clicking weekly radio button
     */

    @FXML
    public void weeklyFilterButton(ActionEvent event) {

        noFilterButton.setSelected(false);
        monthlyFilterButton.setSelected(false);

        appointmentTable.setItems(AppointmentDB.getWeeklyAppointments());
    }

    /**
     * This method generates appointment sorted by monthly in the DB
     * @param event by clicking monthly radio button
     */

    @FXML
    public void monthlyFilterButton(ActionEvent event) {
        noFilterButton.setSelected(false);
        weeklyFilterButton.setSelected(false);

        appointmentTable.setItems(AppointmentDB.getMonthlyAppointments());
    }

    /**
     * This method isolate each filter button individually to ensure one selection at the time
     */

    public void toggleView() {

        filterToggleGroup = new ToggleGroup();

        noFilterButton.setToggleGroup(filterToggleGroup);
        weeklyFilterButton.setToggleGroup(filterToggleGroup);
        monthlyFilterButton.setToggleGroup(filterToggleGroup);
    }


    /**
     * This method initializes add Appointment Viewer screen
     * @param url the url
     * @param resourceBundle initializes resource bundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noFilterButton.setSelected(true);

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentTable.setItems(AppointmentDB.getAllAppointments());
    }
}