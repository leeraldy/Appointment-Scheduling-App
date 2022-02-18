package view_controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AppointmentDB;
import model.Customer;
import model.CustomerDB;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * CustomerScene: Handles the main customers screen objects
 * @author Hussein Coulibaly
 */
public class CustomerScene implements Initializable {
    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button backButton;
    @FXML
    TableView<Customer> customerTable;
    @FXML
    TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    TableColumn<Customer, String> customerNameColumn;
    @FXML
    TableColumn<Customer, String> countryColumn;
    @FXML
    TableColumn<Customer, String> divisionColumn;
    @FXML
    TableColumn<Customer, String> addressColumn;
    @FXML
    TableColumn<Customer, String> postalCodeColumn;
    @FXML
    TableColumn<Customer, String> phoneNumberColumn;

    /**
     * Populates next screen
     *
     * @param event Button Click
     * @param switchPath Path to next scene
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
     *
     * Redirects to Customer scene
     *
     * @param inputList list of customers
     */
    public void populateCustomers(ObservableList<Customer> inputList) {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerTable.setItems(inputList);

    }

    /**
     * Redirects screen to add customer in the Database
     *
     * @param event Button Press
     * @throws IOException
     */
    public void pressAddButton(ActionEvent event) throws IOException {
        switchScreen(event, "/view_controller/AddCustomer.fxml");

    }

    /**
     * @param event Button Press
     * @throws IOException
     * @throws SQLException
     */
    public void pressEditButton(ActionEvent event) throws IOException, SQLException {

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "No Customer selected", clickOkay);
            alert.showAndWait();
            return;

        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/ModifyCustomer.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        ModifyCustomer controller = loader.getController();
        controller.initData(selectedCustomer);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);

    }

    /**
     * Removes the selected object
     * @param event Button Press
     * @throws IOException
     * @throws SQLException
     */
    public void pressDeleteButton(ActionEvent event) throws IOException, SQLException {

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "No selected Customer", clickOkay);
            alert.showAndWait();
            return;
        }
        else {
            ButtonType clickYes = ButtonType.YES;
            ButtonType clickNo = ButtonType.NO;
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this customer: "
                    + selectedCustomer.getCustomerID() + " and all related appointments?", clickYes, clickNo);
            Optional<ButtonType> result = deleteAlert.showAndWait();


            if (result.get() == ButtonType.YES) {
                Boolean customerApptSuccess = AppointmentDB.cancelCustomersAppointments(selectedCustomer.getCustomerID());

                Boolean customerSuccess = CustomerDB.removeCustomer(selectedCustomer.getCustomerID());


                if (customerSuccess && customerApptSuccess) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deletedCustomer = new Alert(Alert.AlertType.CONFIRMATION,
                            "Customer + related appointments deleted", clickOkay);
                    deletedCustomer.showAndWait();

                }
                else {

                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deleteAppt = new Alert(Alert.AlertType.WARNING,
                            "Failed to delete Customer or related appointments ", clickOkay);
                    deleteAppt.showAndWait();

                }

                // Updates appointments on screen
                try {
                    populateCustomers(CustomerDB.getAllCustomersList());
                }
                catch (SQLException error){
                    error.printStackTrace();
                }

            }
            else {
                return;
            }
        }
    }

    /**
     * Returns back previous scene
     *
     * @param event ButtonClick
     * @throws IOException
     */
    public void pressBackButton(ActionEvent event) throws IOException {

        switchScreen(event, "/view_controller/AppointmentScene.fxml");

    }

    /**
     * Returns the user to the mainscreen
     *
     * @param location user location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            populateCustomers(CustomerDB.getAllCustomersList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
