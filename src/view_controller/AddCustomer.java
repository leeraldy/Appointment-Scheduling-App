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
 * AddCustomer Class: Handles customers addition.
 * @author Hussein Coulibaly
 */
public class AddCustomer implements Initializable {
    @FXML
    TextField customerIDTextField;
    @FXML
    ComboBox<String> countryComboBox;
    @FXML
    ComboBox<String> divisionComboBox;
    @FXML
    TextField customerNameTextField;
    @FXML
    TextField addressTextField;
    @FXML
    TextField postalCodeTextField;
    @FXML
    TextField phoneNumberTextField;
    @FXML
    Button saveButton;
    @FXML
    Button clearButton;
    @FXML
    Button backButton;



    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void pressSaveButton(ActionEvent event) throws SQLException, IOException {
        // INPUT VALIDATION - if no nulls found
        String country = countryComboBox.getValue();
        String division = divisionComboBox.getValue();
        String name = customerNameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneNumberTextField.getText();

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
                CustomerDB.obtainParticularDivisionID(division));

        // Generates successfully added message, if no error found.
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


    public void pressClearButton(ActionEvent event) {
        countryComboBox.getItems().clear();
        divisionComboBox.getItems().clear();
        customerNameTextField.clear();
        addressTextField.clear();
        postalCodeTextField.clear();
        phoneNumberTextField.clear();

    }


    public void pressBackButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/CustomerScene.fxml");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(CustomerDB.getAllCountriesList());
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
                    divisionComboBox.setItems(CustomerDB.getFilteredDivisionsView(countryComboBox.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

    }
}