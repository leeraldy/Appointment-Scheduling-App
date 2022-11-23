package controller;

import utils.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * CustomerViewer Class: This class handles customer viewer scren
 * @author Hussein Coulibaly
 */



public class CustomerViewer implements Initializable {

    Stage stage;
    Parent scene;

    @FXML Button backButton;
    @FXML Button saveButton;
    @FXML Button editButton;
    @FXML Button deleteButton;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, Integer> divisionColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> postalCodeColumn;
    @FXML private TableColumn<Customer, String> phoneNumberColumn;


    /**
     * This method redirects the end user to the main console
     * @param event by clicking the return button
     * @throws IOException return an error if found
     */

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainConsole.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method adds a new customer to the DB
     * @param event by clicking new button
     * @throws IOException returns an error if found
     */

    @FXML
    public void addButtonHandler(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * This method deletes an existing customer and all related appointment saved in the DB
     * @param event by clicking delete button
     * @throws IOException return an error if found
     */

    @FXML
    public void deleteButtonHandler(ActionEvent event) throws IOException {

        System.out.println("Delete button was pressed");

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Delete customer?");
            alert.setContentText("No customer was selected to delete.");

            Optional<ButtonType> result = alert.showAndWait();
        }

        else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Delete customer?");
            alert.setContentText("Deleting this customer is permanent and cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int success = customerTable.getSelectionModel().getSelectedItem().getCustomerID();

                CustomerDB.deleteCustomer(success);

                customerTable.setItems(CustomerDB.getAllCustomers());

                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setHeaderText("Delete customer?");
                successAlert.setContentText("Customer " + selectedCustomer.getCustomerID() + " was successfully deleted and all related appointment");

                successAlert.showAndWait();
            }
            else
            {
                Alert delAlert = new Alert(Alert.AlertType.INFORMATION);
                delAlert.setHeaderText("Delete customer?");
                delAlert.setContentText("The selected customer was not deleted.");

                delAlert.showAndWait();
            }
        }
    }

    /**
     * This method edits existing customer in the DB
     * @param event by clicking edit button
     * @throws IOException returns an error if found
     */

    @FXML
    public void editButtonHandler(ActionEvent event) throws IOException, SQLException {

        System.out.println("Edit Button was pressed");

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Edit customer?");
            alert.setContentText("No customer was selected to delete.");

            Optional<ButtonType> result = alert.showAndWait();
        }

        else
        {


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/EditCustomer.fxml"));
            loader.load();
            EditCustomer controller = loader.getController();
            controller.MetaData(customerTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }

    /**
     * This method initializes customer viewer screen
     * @param url the url
     * @param resourceBundle initializes resource bundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(CustomerDB.getAllCustomers());
    }
}