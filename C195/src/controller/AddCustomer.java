package controller;

import utils.CountryDB;
import utils.CustomerDB;
import utils.DivisionDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddCustomer Class: This class handles adding customer to the DB
 *
 * @author Hussein Coulibaly
 */


public class AddCustomer implements Initializable {
    Stage stage;
    Parent scene;

    @FXML private TextField customerIDTextField;
    @FXML private TextField customerNameTextField;
    @FXML private ComboBox<Country> countryComboBox;
    @FXML private ComboBox<Division> divisionComboBox;
    @FXML private TextField addressTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private Label divisionSwitchLabel;

    /**
     * This method cancels an add customer action
     *
     * @param event by clicking cancel button
     * @throws IOException returns an error if found
     */

    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Quit adding?");
        alert.setContentText("Changes you made so far will not be saved");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/CustomerViewer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method add customer by validating all the fields are not empty
     * @param event by clicking save button
     * @throws IOException return an error if found
     */

    @FXML
    public void saveButtonHandler(ActionEvent event) throws IOException {


            String customerName = customerNameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            Division division = divisionComboBox.getValue();
            Country country = countryComboBox.getValue();

            if ((division == null) || (country == null) || customerName.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()) {

                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setHeaderText("Missing Fields");
                emptyAlert.setContentText("Please all fields are required!");
                emptyAlert.showAndWait();

            }
            else
            {
                CustomerDB.addCustomer(customerName, address, postalCode, phone, division.getDivisionID());

                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setHeaderText("Confirmation?");
                successAlert.setContentText("Customer added successfully!");
                successAlert.showAndWait();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/CustomerViewer.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

    }


    /**
     * This method generates division by ID
     *
     * @param event when selection is made in the division field
     */

    @FXML
    public void divisionViewer(ActionEvent event)
    {

        Country country = countryComboBox.getSelectionModel().getSelectedItem();

        if (country.getCountryID() == 1)
        {
            divisionSwitchLabel.setText("State:");
        }
        else if (country.getCountryID() == 2)
        {
            divisionSwitchLabel.setText("Sub-division:");
        }
        else if (country.getCountryID() == 3)
        {
            divisionSwitchLabel.setText("Province:");
        }

        if (country.getCountryID() == 1)
        {
            divisionComboBox.setItems(DivisionDB.getUSDivisions());
        }
        else if (country.getCountryID() == 2)
        {
            divisionComboBox.setItems(DivisionDB.getUKDivisions());
        }
        else if (country.getCountryID() == 3)
        {
            divisionComboBox.setItems(DivisionDB.getCADivisions());
        }
        else
        {
            divisionComboBox.isDisabled();
        }
    }

    /**
     * This method initializes add customer screen
     * @param url the url
     * @param resourceBundle initializes resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        countryComboBox.setItems(CountryDB.getAllCountries());
        divisionComboBox.getSelectionModel().clearSelection();
    }
}