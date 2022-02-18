package view_controller;

import javafx.event.*;

import java.io.IOException;
import java.lang.*;
import java.lang.Integer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerDB;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * ModifyCustomer Class: Handles the changes of the current customer
 * @author Hussein Coulibaly
 */
public class ModifyCustomer implements Initializable {

    @FXML
    TextField customerIDTextBox;
    @FXML
    ComboBox<String> countryComboBox;
    @FXML
    ComboBox<String> divisionComboBox;
    @FXML
    TextField nameTextBox;
    @FXML
    TextField addressTextBox;
    @FXML
    TextField postalCodeTextBox;
    @FXML
    TextField phoneTextBox;
    @FXML
    Button saveButton;
    @FXML
    Button clearButton;
    @FXML
    Button backButton;

    /**
     * Set initiates modify scene
     *
     * @param selectedCustomer Customer object from previous scene
     * @throws SQLException
     */
    public void initData(Customer selectedCustomer) throws SQLException {

        countryComboBox.setItems(CustomerDB.getAllCountriesList());
        countryComboBox.getSelectionModel().select(selectedCustomer.getCountry());
        divisionComboBox.setItems(CustomerDB.getFilteredDivisionsView(selectedCustomer.getCountry()));
        divisionComboBox.getSelectionModel().select(selectedCustomer.getDivision());

        customerIDTextBox.setText(selectedCustomer.getCustomerID().toString());
        nameTextBox.setText(selectedCustomer.getName());
        addressTextBox.setText(selectedCustomer.getAddress());
        postalCodeTextBox.setText(selectedCustomer.getPostalCode());
        phoneTextBox.setText(selectedCustomer.getPhoneNumber());


    }


    public void switchScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void pressSaveButton(ActionEvent event) throws IOException, SQLException {
        // INPUT VALIDATION
        String country = countryComboBox.getValue();
        String division = divisionComboBox.getValue();
        String name = nameTextBox.getText();
        String address = addressTextBox.getText();
        String postalCode = postalCodeTextBox.getText();
        String phone = phoneTextBox.getText();
        Integer customerID = Integer.parseInt(customerIDTextBox.getText());

        if (country.isBlank() || division.isBlank() || name.isBlank() || address.isBlank() || postalCode.isBlank() ||
                phone.isBlank()) {

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert emptyVal = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are completed.",
                    clickOkay);
            emptyVal.showAndWait();
            return;

        }

        // Updates customers in Database
        Boolean success = CustomerDB.updateCustomer(division, name, address, postalCode, phone, customerID);

        if (success) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment updated successfully!", clickOkay);
            alert.showAndWait();
            switchScreen(event, "/view_controller/CustomerScene.fxml");
        }
        else {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "failed to Update appointment", clickOkay);
            invalidInput.showAndWait();
        }
    }

    /**
     * clears fields on screen
     *
     * @param event Button Press
     */
    public void pressClearButton(ActionEvent event) {
        countryComboBox.getSelectionModel().clearSelection();
        divisionComboBox.getSelectionModel().clearSelection();
        nameTextBox.clear();
        addressTextBox.clear();
        postalCodeTextBox.clear();
        phoneTextBox.clear();
    }


    public void pressBackButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/CustomerScene.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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