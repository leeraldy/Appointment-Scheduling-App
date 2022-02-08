package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.CustomerDB;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This controller handles customers addition.
 * @author Hussein Coulibaly
 */
public class AddCustomer implements Initializable {
    @FXML
    TextField customerIDTextBox;
    @FXML
    ComboBox<String> countryComboBox;
    @FXML
    ComboBox<String> divisionComboBox;
    @FXML
    TextField customerNameTextBox;
    @FXML
    TextField addressTextBox;
    @FXML
    TextField postalCodeTextBox;
    @FXML
    TextField phoneNumberTextBox;
    @FXML
    Button saveButton;
    @FXML
    Button clearButton;
    @FXML
    Button backButton;

    /**
     * Sets initial scene to add customers
     *
     * @param event button press
     * @param switchPath path to new stage
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
     * Validates inputs
     *
     * @param event Button press
     * @throws SQLException
     * @throws IOException
     */
    public void pressSaveButton(ActionEvent event) throws SQLException, IOException {
        // INPUT VALIDATION - if no nulls found
        String country = countryComboBox.getValue();
        String division = divisionComboBox.getValue();
        String name = customerNameTextBox.getText();
        String address = addressTextBox.getText();
        String postalCode = postalCodeTextBox.getText();
        String phone = phoneNumberTextBox.getText();

        if (country.isBlank() || division.isBlank() || name.isBlank() || address.isBlank() || postalCode.isBlank() ||
                phone.isBlank()) {

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert emptyVal = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are completed.",
                    clickOkay);
            emptyVal.showAndWait();
            return;

        }

        // Add new customer to Database
        Boolean success = CustomerDB.addCustomer(country, division, name, address, postalCode, phone,
                CustomerDB.getSpecificDivisionID(division));

        // Generates successfully added message, there's error found.
        if (success) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer added successfully!", clickOkay);
            alert.showAndWait();
            pressClearButton(event);
            switchScreen(event, "/view_controller/CustomerScene.fxml");
        }
        else {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to add Customer", clickOkay);
            alert.showAndWait();
            return;
        }

    }

    /**
     * Removes fields on scene when clcked
     *
     * @param event Button Click
     */
    public void pressClearButton(ActionEvent event) {
        countryComboBox.getItems().clear();
        divisionComboBox.getItems().clear();
        customerNameTextBox.clear();
        addressTextBox.clear();
        postalCodeTextBox.clear();
        phoneNumberTextBox.clear();

    }

    /**
     * Return to previous screen
     * @param event Button Click
     */
    public void pressBackButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/CustomerScene.fxml");

    }


    /**
     * Lambda expression - creates a listener for changes in a ComboBox
     *
     * @param url path of scene
     * @param resourceBundle finds root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(CustomerDB.getAllCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Lambda Expression - Listener for combo box change
        countryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                divisionComboBox.getItems().clear();
                divisionComboBox.setDisable(true);

            }
            else {
                divisionComboBox.setDisable(false);
                try {
                    divisionComboBox.setItems(CustomerDB.getFilteredDivisions(countryComboBox.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

    }
}