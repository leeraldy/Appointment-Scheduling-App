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
 * ContactDB Class: Manages all the contact in Database
 *
 * @author Hussein Coulibaly
 */
public class ContactDB {

    // Pulls all contact from the Database
    public static Integer getMinutesTimetable(String contactID) throws SQLException {

        int allCombinedMins = 0;
        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "SELECT * FROM appointments WHERE Contact_ID = ?");

        ps.setString(1, contactID);

        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
            allCombinedMins += (int)Duration.between(startDateTime, endDateTime).toMinutes();
        }

        ps.close();
        return allCombinedMins;
    }


    public static ObservableList<String> getContactAppts(String contactID) throws SQLException {
        ObservableList<String> apptStr = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "SELECT * FROM appointments WHERE Contact_ID = ?");

        ps.setString(1, contactID);

        ResultSet rs = ps.executeQuery();

        while ( rs.next()) {
            String apptID = rs.getString("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            String customerID = rs.getString("Customer_ID");

            String newLine = "  AppointmentID: " + apptID + "\n";
            newLine += "        Title: " + title + "\n";
            newLine += "        Type: " + type + "\n";
            newLine += "        Start date/time: " + start + " UTC\n";
            newLine += "        End date/time: " + end + " UTC\n";
            newLine += "        CustomerID: " + customerID + "\n";

            apptStr.add(newLine);

        }

        ps.close();
        return apptStr;

    }


    public static ObservableList<String> getAllContactByName() throws SQLException {
        ObservableList<String> allContactName = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT DISTINCT Contact_Name" +
                " FROM contacts;");
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allContactName.add(rs.getString("Contact_Name"));
        }
        ps.close();
        return allContactName;
    }


    public static Integer obtainContactID(String contactName) throws SQLException {

        int contactID = -1;
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Contact_ID, Contact_Name " +
                "FROM contacts WHERE Contact_Name = ?");
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            contactID = rs.getInt("Contact_ID");
        }
        ps.close();
        return contactID;


    }
}