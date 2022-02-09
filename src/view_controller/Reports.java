package view_controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.AppointmentDB;
import model.ContactDB;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles reports that are recorded
 * @author Hussein Coulibaly
 */
public class Reports implements Initializable {

    @FXML
    Button ApptByReportButton;
    @FXML
    Button contactScheduleReportButton;
    @FXML
    Button minsPerContactButton;
    @FXML
    TextArea reportTextField;
    @FXML
    Button backButton;

    /**
     * Initiates new scene
     *
     * @param event Button Press
     * @param switchPath path to new scene
     * @throws IOException
     */
    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Sets screen to previous scene
     *
     * @param event Button Press
     * @throws IOException
     */
    public void pressBackButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AppointmentScene.fxml");

    }

    /**
     * Populates the first report
     *
     * @param event Button Press
     * @throws SQLException
     */
    public void pressApptByReportButton(ActionEvent event) throws SQLException {

        ObservableList<String> reportStrings = AppointmentDB.reportTotalsByTypeAndMonth();

        for (String str : reportStrings) {
            reportTextField.appendText(str);
        }

    }

    /**
     * Populates second report
     *
     * @param event Button Press
     * @throws SQLException
     */
    public void pressMinsPerContact(ActionEvent event ) throws SQLException {
        ObservableList<String> contacts = ContactDB.getAllContactName();

        for (String contact: contacts) {
            String contactID = ContactDB.findContactID(contact).toString();
            reportTextField.appendText("Contact Name: " + contact + " ID: " + contactID + "\n");
            reportTextField.appendText("    Total Mins scheduled: " + ContactDB.getMinutesScheduled(contactID) + "\n");
        }
    }

    /**
     * Populates 3rd report
     *
     * @param event Button Click
     * @throws SQLException
     */
    public void pressContactSchedule(ActionEvent event) throws SQLException {

        ObservableList<String> contacts = ContactDB.getAllContactName();

        for (String contact : contacts) {
            String contactID = ContactDB.findContactID(contact).toString();
            reportTextField.appendText("Contact Name: " + contact + " ID: " + contactID + "\n");

            ObservableList<String> appts = ContactDB.getContactAppts(contactID);
            if(appts.isEmpty()) {
                reportTextField.appendText("    No appointments for contact \n");
            }
            for (String appt : appts) {
                reportTextField.appendText(appt);
            }

        }
    }


    /**
     * Populates main screen
     *
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }
}