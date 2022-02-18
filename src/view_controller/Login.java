package view_controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import model.Appointment;
import model.AppointmentDB;
import model.LoginSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import utils.Logger;

import java.net.URL;

/**
 *
 * Login Class: Manages the user login
 *
 * @author Hussein Coulibaly
 */
public class Login implements Initializable {
    @ FXML
    private TextField passwordTextField;
    @ FXML
    private TextField userTextField;
    @ FXML
    private Label titleLabel;
    @ FXML
    private Label userNameLabel;
    @ FXML
    private Label passwordLabel;
    @ FXML
    private Button loginButton;
    @ FXML
    private Button clearButton;
    @ FXML
    private Button exitButton;
    @ FXML
    private Label zoneLabel;


    public void switchScreen(ActionEvent event, String switchPath) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();


    }


    public void pressLogonButton(ActionEvent event) throws IOException, SQLException {
        String userName = userTextField.getText();
        String password = passwordTextField.getText();

        // Attempt Login
        boolean logon = LoginSession.accessAttempt(userName, password);

        // Log Login attempt
        Logger.log(userName, logon);

        if (logon) {


            ObservableList<Appointment> upcomingAppts = FXCollections.observableArrayList();
            for(Appointment a: AppointmentDB.getAllAppointments()) {
                if(a.getStartDateTime().toLocalDateTime().isAfter(LocalDateTime.now()) && a.getStartDateTime().toLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(15)))
                {
                    upcomingAppts.add(a);
                }
            }
                if (!upcomingAppts.isEmpty()) {
                for (Appointment upcoming : upcomingAppts) {

                    String message = "Upcoming appointmentID: " + upcoming.getAppointmentID() + " Start: " +
                            upcoming.getStartDateTime().toString();
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, message, clickOkay);
                    invalidInput.showAndWait();

                }

            }
            // Generate a notification no upcoming appointments if no appointments are found within 15 minutes.
            else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.CONFIRMATION, "There's no upcoming Appointments", clickOkay);
                invalidInput.showAndWait();
            }

            switchScreen(event, "/view_controller/AppointmentScene.fxml");

        }
        else {
            Locale userLocale = Locale.getDefault();
            ResourceBundle resources = ResourceBundle.getBundle("language/login");
            ButtonType clickOkay = new ButtonType(resources.getString("okayButton"), ButtonBar.ButtonData.OK_DONE);
            Alert failedLogon = new Alert(Alert.AlertType.WARNING, resources.getString("logonFailedButton"),
                    clickOkay);
            failedLogon.showAndWait();
        }

    }

    /**
     * Clears any input when clicked
     * @param event Button Pressed
     * @throws IOException
     */
    public void pressClearButton(ActionEvent event) throws IOException {
        userTextField.clear();
        passwordTextField.clear();

    }


    public void pressExitButton(ActionEvent event) throws IOException {
        LoginSession.SignOff();
        System.exit(0);

    }

    /**
     * Returns the user to the mainscreen
     *
     * @param location Time zone
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Locale userLocale = Locale.getDefault();
        zoneLabel.setText(ZoneId.systemDefault().toString());
        resources = ResourceBundle.getBundle("language/login");
        titleLabel.setText(resources.getString("titleLabel"));
        userNameLabel.setText(resources.getString("userNameLabel"));
        passwordLabel.setText(resources.getString("passwordLabel"));
        loginButton.setText(resources.getString("loginButton"));
        clearButton.setText(resources.getString("clearButton"));
        exitButton.setText(resources.getString("exitButton"));




    }

}
