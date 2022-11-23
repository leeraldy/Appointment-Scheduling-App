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
import model.Customer;
import model.Division;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * EditCustomer Class: This class handles all editable customer objects
 * @author Hussein Coulibaly
 */


public class EditCustomer implements Initializable {

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
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
   Customer customer;

    /**
     * This method cancels an editing customer action
     *
     * @param event by clicking cancel button
     * @throws IOException returns an error if found
     */


    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Cancel action?");
        alert.setContentText("Do you want to cancel updating this customer?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/CustomerViewer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /**
     * This method saves an edited customer in the DB
     * @param event by clicking save button customer is saved
     * @throws IOException returns an error if one is found
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
                emptyAlert.setHeaderText("Missing fields");
                emptyAlert.setContentText("Please all fields are required!");
                emptyAlert.showAndWait();

            }
            else {
                CustomerDB.updateCustomer(customerName, address, postalCode, phone, division.getDivisionID(), customer.getCustomerID());

                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setHeaderText("Confirmation");
                successAlert.setContentText("Customer updated successfully!");
                successAlert.showAndWait();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/CustomerViewer.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }


    /**
     * This method populates division by ID to the editCustomer screen
     * @param event by selecting country in the editCustomer screen
     */

    @FXML
    public void filterDivision(ActionEvent event) {

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
     * This method populates the edited customer in the customer viewer
     * @param selectedCustomer generates the updated customer
     */

    public void MetaData(Customer selectedCustomer) {

        this.customer = selectedCustomer;

        customerIDTextField.setText(Integer.toString(selectedCustomer.getCustomerID()));
        customerNameTextField.setText(selectedCustomer.getCustomerName());
        addressTextField.setText(selectedCustomer.getAddress());
        postalCodeTextField.setText(selectedCustomer.getPostalCode());
        phoneNumberTextField.setText(selectedCustomer.getPhone());

        for (Country country : countryComboBox.getItems())
        {
            if (selectedCustomer.countryID == country.getCountryID())
            {
                countryComboBox.setValue(country);
                break;
            }
        }

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

        for (Division division : divisionComboBox.getItems())
        {
            if (selectedCustomer.divisionID == division.getDivisionID())
            {
                divisionComboBox.setValue(division);
                break;
            }
        }

    }

    /**
     * This method initializes editCustomer screen
     * @param url the url
     * @param resourceBundle initializes resource bundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        countryComboBox.setItems(CountryDB.getAllCountries());
    }
}
