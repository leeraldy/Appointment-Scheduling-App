package utils;

import model.Customer;
import utilities.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.format.DateTimeFormatter;


/**
 * This class handles all customers in the DB
 *
 */

public class CustomerDB {

    /**
     * This method handles all customers objects in the DB
     * @return
     */
    public static ObservableList<Customer> getAllCustomers()
    {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, " +
                    "first_level_divisions.COUNTRY_ID, first_level_divisions.Division FROM customers, first_level_divisions WHERE customers.Division_ID = first_level_divisions.Division_ID ORDER BY Customer_ID");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");
                int countryID = rs.getInt("COUNTRY_ID");
                String divisionName = rs.getString("Division");

                Customer c = new Customer(customerID, customerName, address, postalCode, phone, divisionID, countryID, divisionName);
                allCustomers.add(c);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return allCustomers;
    }

    /**
     * This method allows to update customer information in the DB
     *
     */
    public static void updateCustomer(String customerName, String address, String postalCode, String phone, int divisionID, int customerID)
    {
        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?");

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone  );
            ps.setInt(5, divisionID);
            ps.setInt(6, customerID);

            ps.execute();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * This method allows to add customer in the DB
     *
     * @param customerName holds customer name data
     * @param address holds customer address data
     * @param postalCode holds data
     * @param phone holds customer phone number data
     * @param divisionID holds customer by divisionID
     */

    public static void addCustomer(String customerName, String address, String postalCode, String phone, int divisionID) {

    DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("INSERT INTO customers VALUES (NULL, ?, ?, ?, ?, NOW(), 'RZ', NOW(), 'RZ', ?)");

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionID);

            ps.execute();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method allows to delete customer by selecting the customer ID
     *
     * @param customerID holds customer ID
     */

    public static void deleteCustomer(int customerID) {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE from appointments where Customer_ID = ?");

            ps.setInt(1, customerID);

            ps.execute();


            PreparedStatement psa = DBConnection.dbConn().prepareStatement("DELETE FROM customers where Customer_ID = ?");

            psa.setInt(1, customerID);

            psa.execute();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
