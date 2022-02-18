package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CustomerDB Class: Manages customers Objects in the Database
 *
 * @author Hussein Coulibaly
 */
public class CustomerDB {


    public static Boolean updateCustomer( String division, String name, String address,
                                          String postalCode, String phoneNum, Integer customerID) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement ps = DBConnection.dbConn().prepareStatement("UPDATE customers "
                + "SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Last_Update=?," +
                " Last_Updated_By=?, Division_ID=? WHERE Customer_ID = ?");

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(6, LoginSession.getLoginUser().getUserName());
        ps.setInt(7, CustomerDB.obtainParticularDivisionID(division));
        ps.setInt(8, customerID);


        try {
            ps.executeUpdate();
            ps.close();
            return true;
        }
        catch (SQLException throwables) {
            //TODO- log error
            throwables.printStackTrace();
            ps.close();
            return false;
        }

    }


    public static Boolean removeCustomer(Integer customerID) throws SQLException {
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE FROM customers " +
                "WHERE Customer_ID = ?");

        ps.setInt(1, customerID);

        try {
            ps.executeUpdate();
            ps.close();
            return true;
        }
        catch (SQLException throwables) {
            //TODO- log error
            throwables.printStackTrace();
            return false;
        }
    }


    public static Boolean addCustomer(String country, String division, String name, String address, String postalCode,
                                      String phoneNum, Integer divisionID) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, " +
                        "Last_Update, Last_Updated_By, Division_ID) \n" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(6, LoginSession.getLoginUser().getUserName());
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(8, LoginSession.getLoginUser().getUserName());
        ps.setInt(9, divisionID);


        try {
            ps.executeUpdate();
            ps.close();
            return true;
        }
        catch (SQLException throwables) {
            //TODO- log error
            throwables.printStackTrace();
            ps.close();
            return false;
        }

    }


    public static Integer obtainParticularDivisionID(String division) throws SQLException {
        int divID = 0;
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Division, Division_ID FROM " +
                "first_level_divisions WHERE Division = ?");

        ps.setString(1, division);

        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            divID = rs.getInt("Division_ID");
        }

        ps.close();
        return divID;

    }


    public static ObservableList<Integer> getAllCustomerID() throws SQLException {

        ObservableList<Integer> allCustomerID = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT DISTINCT Customer_ID" +
                " FROM customers;");
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allCustomerID.add(rs.getInt("Customer_ID"));
        }
        ps.close();
        return allCustomerID;
    }


    public static ObservableList<String> getFilteredDivisionsView(String inCountry) throws SQLException {

        ObservableList<String> filteredDivs = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "SELECT c.Country, c.Country_ID,  d.Division_ID, d.Division FROM countries as c RIGHT OUTER JOIN " +
                        "first_level_divisions AS d ON c.Country_ID = d.Country_ID WHERE c.Country = ?");

        ps.setString(1, inCountry);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            filteredDivs.add(rs.getString("Division"));
        }

        ps.close();
        return filteredDivs;

    }


    public static ObservableList<String> getAllCountriesList() throws SQLException {

        ObservableList<String> allCountries = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT DISTINCT Country FROM countries");
        ResultSet results = ps.executeQuery();

        while (results.next()) {
            allCountries.add(results.getString("Country"));
        }
        ps.close();
        return allCountries;

    }


    public static ObservableList<Customer> getAllCustomersList() throws SQLException {

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "SELECT cx.Customer_ID, cx.Customer_Name, cx.Address, cx.Postal_Code, cx.Phone, cx.Division_ID, " +
                        "f.Division, f.COUNTRY_ID, co.Country FROM customers as cx INNER JOIN first_level_divisions " +
                        "as f on cx.Division_ID = f.Division_ID INNER JOIN countries as co ON f.COUNTRY_ID = co.Country_ID");
        ResultSet rs = ps.executeQuery();


        while( rs.next() ) {

            Integer custID = rs.getInt("Customer_ID");
            String custName = rs.getString("Customer_Name");
            String custAddress = rs.getString("Address");
            String custPostalCode = rs.getString("Postal_Code");
            String custPhoneNum = rs.getString("Phone");
            String custDivision = rs.getString("Division");
            Integer divID = rs.getInt("Division_ID");
            String custCountry = rs.getString("Country");


            Customer newCust = new Customer(custID, custName, custAddress, custPostalCode, custPhoneNum, custDivision,
                    divID, custCountry);


            allCustomers.add(newCust);

        }
        ps.close();
        return allCustomers;

    }
}
