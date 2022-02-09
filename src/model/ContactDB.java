package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ContactDB Class: Handles all the contact in Database
 *
 * @author Hussein Coulibaly
 */
public class ContactDB {

    /**
     * Gets the total sum of minutes for all appointments from a specific contact
     *
     * @param contactID Contact ID to find the total sum
     * @return returns the total sum of number of minutes scheduled
     * @throws SQLException
     */
    public static Integer getMinutesScheduled(String contactID) throws SQLException {

        Integer totalMins = 0;
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT * FROM appointments WHERE Contact_ID = ?");

        sqlCommand.setString(1, contactID);

        ResultSet results = sqlCommand.executeQuery();

        while ( results.next() ) {
            LocalDateTime startDateTime = results.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDateTime = results.getTimestamp("End").toLocalDateTime();
            totalMins += (int)Duration.between(startDateTime, endDateTime).toMinutes();
        }

        sqlCommand.close();
        return totalMins;
    }

    /**
     * Gets all appointments from a specific contact
     *
     * @param contactID Contact ID to retrieve appointments
     * @return returns ObservableList of all appointments from a specific contact
     * @throws SQLException
     */
    public static ObservableList<String> getContactAppts(String contactID) throws SQLException {
        ObservableList<String> apptStr = FXCollections.observableArrayList();
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT * FROM appointments WHERE Contact_ID = ?");

        sqlCommand.setString(1, contactID);

        ResultSet results = sqlCommand.executeQuery();

        while ( results.next()) {
            String apptID = results.getString("Appointment_ID");
            String title = results.getString("Title");
            String type = results.getString("Type");
            String start = results.getString("Start");
            String end = results.getString("End");
            String customerID = results.getString("Customer_ID");

            String newLine = "  AppointmentID: " + apptID + "\n";
            newLine += "        Title: " + title + "\n";
            newLine += "        Type: " + type + "\n";
            newLine += "        Start date/time: " + start + " UTC\n";
            newLine += "        End date/time: " + end + " UTC\n";
            newLine += "        CustomerID: " + customerID + "\n";

            apptStr.add(newLine);

        }

        sqlCommand.close();
        return apptStr;

    }

    /**
     * Retrieves all contacts name
     *
     * @return returns Observablelist of all contact names
     * @throws SQLException
     */
    public static ObservableList<String> getAllContactName() throws SQLException {
        ObservableList<String> allContactName = FXCollections.observableArrayList();
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("SELECT DISTINCT Contact_Name" +
                " FROM contacts;");
        ResultSet results = sqlCommand.executeQuery();

        while ( results.next() ) {
            allContactName.add(results.getString("Contact_Name"));
        }
        sqlCommand.close();
        return allContactName;
    }

    /**
     * findContactID
     * Gets the contact name and associates the ID to all other operations to that contact
     *
     * @param contactName contact name of the ID that has been searched
     * @return returns a Contact ID that corresponds
     * @throws SQLException
     */
    public static Integer findContactID(String contactName) throws SQLException {

        Integer contactID = -1;
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("SELECT Contact_ID, Contact_Name " +
                "FROM contacts WHERE Contact_Name = ?");
        sqlCommand.setString(1, contactName);
        ResultSet results = sqlCommand.executeQuery();

        while (results.next()) {
            contactID = results.getInt("Contact_ID");
        }
        sqlCommand.close();
        return contactID;


    }
}