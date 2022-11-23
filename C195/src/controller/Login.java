package controller;

import utilities.Logger;
import utils.AppointmentDB;
import utils.LoginSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Appointment;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Login Class: Manages the user login
 *
 * @author Hussein Coulibaly
 */

public class Login implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private Label userNameLabel;
    @FXML private TextField userNameTextField;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordTextField;
    @FXML private Label titleLabel;
    @FXML private Label zoneLabel;
    @FXML private Label zoneIDSwitcherLabel;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    private String exitPrompt;
    private String failedLoginHeader;
    private String failedLoginButton;
    private String confirmPrompt;

    /**
     * This method exits the user out of the application
     *
     * @param event action is executed by clicking the exiting button
     *
     * @throws IOException return error if found
     */

    @FXML
    public void exitButtonHandler(ActionEvent event) throws IOException {

        System.out.println("Exit button was pressed");

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(confirmPrompt);
        alert.setContentText(exitPrompt);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Button)(event.getSource())).getScene().getWindow().hide();
        }

        System.exit(0);

    }


    /**
     * This method sign in the user by validating his/her username and password.
     *
     * Lambda 1 expression is used to check for any upcoming appointment within the 15 minutes
     *
     * @param event action is executed by clicking the exiting button
     *
     * @throws IOException return error if found
     */

    @FXML
    public void signOnButtonHandler(ActionEvent event) throws IOException
    {
        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime local = LocalDateTime.now();
        String localFormat = formatter.format(local);

        int logon = LoginSession.validAccess(userName, password);

        FileWriter ft = new FileWriter("login_activity.txt", true);
        PrintWriter logFile = new PrintWriter(ft);

//        Logger.log(userName, logon);

        if (logon > 0) {

            logFile.println(localFormat + " " + userNameTextField.getText() + " successfully logged in");
            logFile.close();

            LocalDateTime now = LocalDateTime.now();

            ObservableList<Appointment> upcomingAppts = AppointmentDB.getAllAppointments();
            ObservableList<Appointment> upcoming = upcomingAppts.filtered(ap -> ap.getUserID() == logon);

            boolean valid = false;

            for (Appointment a : upcoming) {

                {
                    if (a.getStartTime().toLocalDateTime().isAfter(now) && a.getStartTime().toLocalDateTime().isBefore(now.plusMinutes(15)))
                    {
                        Alert upcomingAlert = new Alert(Alert.AlertType.WARNING);
                        upcomingAlert.setHeaderText("Upcoming Appointment");
                        upcomingAlert.setContentText("You have a scheduled appointment scheduled within the next 15 minutes" + a.getApptID() + " at " + a.getStartTime().toLocalDateTime());
                        upcomingAlert.showAndWait();

                        valid = true;

                    }
                }
            }

            if (!valid)
            {
                Alert noUpcomingAlert = new Alert(Alert.AlertType.WARNING);
                noUpcomingAlert.setHeaderText("No Upcoming Appointments");
                noUpcomingAlert.setContentText("You have no scheduled appointments within the next 15 minutes.");
                noUpcomingAlert.showAndWait();

            }


            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/MainConsole.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {

            logFile.println(localFormat + " " + userNameTextField.getText() + " failed login attempt");
            logFile.close();


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(failedLoginHeader);
            alert.setContentText(failedLoginButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                userNameTextField.clear();
                passwordTextField.clear();
            }
        }
    }

    /**
     * This method sign in the user by validating his/her username and password.
     *
     * Lambda 1 expression is used to check for any upcoming appointment within the 15 minutes
     *
     * @param event action is executed by clicking the exiting button
     *
     * @throws IOException return error if found
     */


    @FXML
    public void loginEnterButton(KeyEvent event) throws IOException {
        if(event.getCode().equals(KeyCode.ENTER)) {

            String userName = userNameTextField.getText();
            String password = passwordTextField.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime local = LocalDateTime.now();
            String timeFormatter = formatter.format(local);

            int logon = LoginSession.validAccess(userName, password);

            FileWriter ft = new FileWriter("login_activity.txt", true);
            PrintWriter logFile = new PrintWriter(ft);

            if (logon > 0) {
                logFile.println(timeFormatter + " " + userNameTextField.getText() + " successfully logged in");
                logFile.close();

                LocalDateTime timeNow = LocalDateTime.now();

                ObservableList<Appointment> upcomingAppts = AppointmentDB.getAllAppointments();
                ObservableList<Appointment> upcoming = upcomingAppts.filtered(ap -> {
                    return ap.getUserID() == logon;
                });

                boolean valid = false;

                for (Appointment a : upcoming)
                {

                    {
                        if (a.getStartTime().toLocalDateTime().isAfter(timeNow) && a.getStartTime().toLocalDateTime().isBefore(timeNow.plusMinutes(15))) {

                            Alert upcomingAlert = new Alert(Alert.AlertType.WARNING);
                            upcomingAlert.setHeaderText("Upcoming Appointment");
                            upcomingAlert.setContentText("You have a scheduled appointment within the next 15 minutes " + a.getApptID() + " at " + a.getStartTime().toLocalDateTime());
                            upcomingAlert.showAndWait();

                            valid = true;
                        }
                    }
                }

                if (!valid) {

                    Alert noUpcomingAlert = new Alert(Alert.AlertType.WARNING);
                    noUpcomingAlert.setHeaderText("No Upcoming Appointment");
                    noUpcomingAlert.setContentText("You have no scheduled appointment within the next 15 minutes.");
                    noUpcomingAlert.showAndWait();

                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/MainConsole.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else {

                logFile.println(timeFormatter + " " + userNameTextField.getText() + " failed login attempt");
                logFile.close();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(failedLoginHeader);
                alert.setContentText(failedLoginButton);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    userNameTextField.clear();
                    passwordTextField.clear();
                }
            }
        }

        }


    /**
     * This method initializes the login screen
     *
     * @param url the URL
     * @param resourceBundle initializes resource bundle
     */


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("languages/language", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                exitPrompt = rb.getString("exitPrompt");
                failedLoginHeader = rb.getString("failedLoginHeader");
                failedLoginButton = rb.getString("failedLoginButton");
                confirmPrompt = rb.getString("confirmPrompt");
                Locale userLocale = Locale.getDefault();
                titleLabel.setText(rb.getString("title"));
                userNameLabel.setText(rb.getString("usernameLabel"));
                passwordLabel.setText(rb.getString("passwordLabel"));
                zoneLabel.setText(ZoneId.systemDefault().toString());
                loginButton.setText(rb.getString("loginButton"));
                exitButton.setText(rb.getString("exit"));
                zoneIDSwitcherLabel.setText((ZoneId.systemDefault()).getId());
            }
        }
        catch (Exception e)
        {
            System.out.println();
        }
    }
}