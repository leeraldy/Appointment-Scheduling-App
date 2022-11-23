package controller;

import utils.AppointmentDB;
import utils.ContactDB;
import utils.CustomerDB;
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
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Reports Class: This class handles all the reports in the application
 *
 * @author Hussein Coulibaly
 */



public class Reports implements Initializable {
    Stage stage;
    Parent scene;

    @FXML private TableColumn<Appointment, Integer> apptIDColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    @FXML private TableColumn<Appointment, Integer> customerIDColumn;
    @FXML private Label apptByMonthAndTypeLabel;
    @FXML private TableView<Appointment> populateContactScheduleData;
    @FXML private Label totalNumberOfCustomerLabel;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private Button backButton;


    /**
     * This method returns the end user to the Main console screen
     * @param event by clicking the return button
     * @throws IOException returns error if any found
     */

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainConsole.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method returns total number of appointments by type and month
     * @param event by clicking the running report button to launch report
     */

    @FXML
    public void apptByMonthAndTypeButtonHandler(ActionEvent event)
    {
        String month = monthComboBox.getValue();
        if (month == null) {
            return;
        }

        String type = typeComboBox.getValue();
        if (type == null) {
            return;
        }

        int total = AppointmentDB.getApptByMonthAndTypeCount(month, type);

        apptByMonthAndTypeLabel.setText(String.valueOf(total));
    }


    /**
     * This method run report to provide details for a particular user
     * @param event by clicking the run report
     * Lambda 2 is used to generates customer appointments by contact ID.
     */

    @FXML
    public void contactScheduleButtonHandler(ActionEvent event) {
        Contact contact = contactComboBox.getValue();

        if (contact == null) {
            return;
        }
        ObservableList<Appointment> allAppointment = AppointmentDB.getAllAppointments();
        ObservableList<Appointment> cList = allAppointment.filtered(ap -> {

            return ap.getContactId() == contact.getContactID();

        });

        populateContactScheduleData.setItems(cList);
    }

    /**
     * This method provides the number of all appointments in the DB
     * @param event by clicking the total number of customer button
     */


    @FXML
    public void totalNumberOfCustomer(ActionEvent event) {

        totalNumberOfCustomerLabel.setText(String.valueOf(CustomerDB.getAllCustomers().size()));
    }


    /**
     * This method initializes the report viewer by running different reports
     * @param url the url
     * @param resourceBundle initializes resources
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        monthComboBox.setItems(FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
        typeComboBox.setItems(AppointmentDB.getApptsFilterByType());
        contactComboBox.setItems(ContactDB.getAllContacts());
    }
}
