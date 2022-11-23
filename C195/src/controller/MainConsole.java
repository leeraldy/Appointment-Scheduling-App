package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainConsole Class: This class handles the main console of all application option
 *
 * @author Hussein Coulibaly
 */

public class MainConsole implements Initializable {
    Stage stage;
    Parent scene;


    /**
     * This method initializes main console screen
     * @param url the url
     * @param resourceBundle initializes resources
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * This method sign off and returns the end user to the main screen
     * @param event by clicking the sign off button as the last option
     * @throws IOException returns an error if any found
     */

    @FXML
    public void logoutButtonHandler(ActionEvent event) throws IOException {

        Alert logoutAlert = new Alert(Alert.AlertType.WARNING);
        logoutAlert.setHeaderText("Logging Off?");
        logoutAlert.setContentText("You about to be signed out, are you sure you want to continue?");

        Optional<ButtonType> result = logoutAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method direct the end user to report viewer screen
     * @param event by clicking report viewer button
     * @throws IOException returns error if any found
     */

    @FXML
    public void reportViewerButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/ReportViewer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method direct the end user to appointment viewer screen
     * @param event by clicking appointment viewer button
     * @throws IOException returns error if any found
     */

    @FXML
    public void apptViewerButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AppointmentViewer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method This method direct the end user to customer viewer screen
     * @param event by clicking customer viewer button
     * @throws IOException returns error if any found
     */

    @FXML
    public void customerViewerButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/CustomerViewer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}


