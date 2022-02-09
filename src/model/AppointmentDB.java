package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AppointmentDB Class: Handles all the SQL queries in Database
 *
 * @author Hussein Coulibaly
 */
public class AppointmentDB {

    /**
     * Gets all the appointment from the DB
     *
     *
     * @param startRange start of range ZonedDateTime
     * @param endRange end of range ZonedDateTime
     * @return Filters all appointments based on an entered start and end date.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getDateFilteredAppointments(ZonedDateTime startRange, ZonedDateTime endRange)
            throws SQLException {

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE" +
                        " Start between ? AND ?"
        );

        String startRangeString = startRange.format(formatter);
        String endRangeString = endRange.format(formatter);

        sqlCommand.setString(1, startRangeString);
        sqlCommand.setString(2, endRangeString);

        ResultSet results = sqlCommand.executeQuery();

        while( results.next() ) {

            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );
            filteredAppts.add(newAppt);
        }

        sqlCommand.close();
        return filteredAppts;

    }

    /**
     * Gets all SQL queries from the database and total the number of appointments for report purposes.
     *
     * @return retuns an ObservableList to generate in the report.
     * @throws SQLException
     */
    public static ObservableList<String> reportTotalsByTypeAndMonth() throws SQLException {
        ObservableList<String> reportStrings = FXCollections.observableArrayList();

        reportStrings.add("Total Number of Appointments by type and month:\n");

        PreparedStatement typeSqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT Type, COUNT(Type) as \"Total\" FROM appointments GROUP BY Type");

        PreparedStatement monthSqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month");

        ResultSet typeResults = typeSqlCommand.executeQuery();
        ResultSet monthResults = monthSqlCommand.executeQuery();

        while (typeResults.next()) {
            String typeStr = "Type: " + typeResults.getString("Type") + " Count: " +
                    typeResults.getString("Total") + "\n";
            reportStrings.add(typeStr);

        }

        while (monthResults.next()) {
            String monthStr = "Month: " + monthResults.getString("Month") + " Count: " +
                    monthResults.getString("Total") + "\n";
            reportStrings.add(monthStr);

        }

        monthSqlCommand.close();
        typeSqlCommand.close();

        return reportStrings;

    }

    /**
     * Gets all SQL queries from all appointments to verify for any conflicts.
     *
     * @param apptDate All possible appointments date
     * @param inputCustomerID Researches for any customer ID
     * @return returns an ObservableList for a specific customer appointment
     * @throws SQLException
     */
    public static ObservableList<Appointment> getCustomerFilteredAppointments(
            LocalDate apptDate, Integer inputCustomerID) throws SQLException {

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement(
                "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c " +
                        "ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0 AND Customer_ID = ?;"
        );

        sqlCommand.setInt(2, inputCustomerID);

        sqlCommand.setString(1, apptDate.toString());

        ResultSet results = sqlCommand.executeQuery();

        while( results.next() ) {
            // get data from the returned rows
            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );
            filteredAppts.add(newAppt);
        }

        sqlCommand.close();
        return filteredAppts;

    }

    /**
     * Gets all the Appointment ID and refreshes it in the database
     *
     * @param inputApptID Retains Appointment ID
     * @param inputTitle Retains appointment title
     * @param inputDescription Retains appointment description
     * @param inputLocation Retains appointment location
     * @param inputType Retains appointment Type
     * @param inputStart Retains appointment Start Time
     * @param inputEnd Retains appointment end Time
     * @param inputLastUpdateBy Retains date/time of update of the appointment
     * @param inputCustomerID Retains Customer ID
     * @param inputUserID Retains User ID
     * @param inputContactID Retains Contact ID
     * @return Boolean displaying that the action was successful
     * @throws SQLException
     */
    public static Boolean updateAppointment(Integer inputApptID, String inputTitle, String inputDescription,
                                            String inputLocation, String inputType, ZonedDateTime inputStart,
                                            ZonedDateTime inputEnd, String inputLastUpdateBy, Integer inputCustomerID,
                                            Integer inputUserID, Integer inputContactID) throws SQLException {

        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("UPDATE appointments "
                + "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?,Last_Updated_By=?, " +
                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStartString = inputStart.format(formatter).toString();
        String inputEndString = inputEnd.format(formatter).toString();

        sqlCommand.setString(1,inputTitle);
        sqlCommand.setString(2, inputDescription);
        sqlCommand.setString(3, inputLocation);
        sqlCommand.setString(4, inputType);
        sqlCommand.setString(5, inputStartString);
        sqlCommand.setString(6, inputEndString);
        sqlCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(8, inputLastUpdateBy);
        sqlCommand.setInt(9, inputCustomerID);
        sqlCommand.setInt(10, inputUserID);
        sqlCommand.setInt(11, inputContactID);
        sqlCommand.setInt(12, inputApptID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            sqlCommand.close();
            return false;
        }

    }

    /**
     * Generates new appointment in the database with entered inputs
     *
     * @param inputTitle appointment title
     * @param inputDescription appointment description
     * @param inputLocation appointment location
     * @param inputType appointment type
     * @param inputStart appointment start time
     * @param inputEnd appointment end time
     * @param inputCreatedBy shows user who sets appointment
     * @param inputLastUpdateBy shows who updated appointment last as the last person
     * @param inputCustomerID customer ID
     * @param inputUserID user ID
     * @param inputContactID contact ID
     * @return returns Boolean displaying a successfully action
     * @throws SQLException
     */
    public static Boolean addAppointment(String inputTitle, String inputDescription,
                                         String inputLocation, String inputType, ZonedDateTime inputStart,
                                         ZonedDateTime inputEnd, String inputCreatedBy,
                                         String inputLastUpdateBy, Integer inputCustomerID,
                                         Integer inputUserID, Integer inputContactID) throws SQLException {

        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Create_date, \n" +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStartString = inputStart.format(formatter).toString();
        String inputEndString = inputEnd.format(formatter).toString();


        sqlCommand.setString(1, inputTitle);
        sqlCommand.setString(2, inputDescription);
        sqlCommand.setString(3, inputLocation);
        sqlCommand.setString(4, inputType);
        sqlCommand.setString(5, inputStartString);
        sqlCommand.setString(6, inputEndString);
        sqlCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(8, inputCreatedBy);
        sqlCommand.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(10, inputLastUpdateBy);
        sqlCommand.setInt(11, inputCustomerID);
        sqlCommand.setInt(12, inputUserID);
        sqlCommand.setInt(13, inputContactID);


        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Deletes appointments
     *
     * @param inputApptID Appointment ID that can be deleted
     * @return returns Boolean displaying a successfully action
     * @throws SQLException
     */
    public static Boolean deleteAppointment(Integer inputApptID) throws SQLException {

        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("DELETE FROM appointments " +
                "WHERE Appointment_ID = ?");

        sqlCommand.setInt(1, inputApptID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Deletes all appointments from the a customer once this last is deleted from the database
     *
     * @param customerID Customer ID for deleting appointments
     * @return returns Boolean displaying a successful action
     * @throws SQLException
     */
    public static Boolean deleteCustomersAppointments(Integer customerID) throws SQLException {

        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("DELETE FROM appointments " +
                "WHERE Customer_ID = ?");

        sqlCommand.setInt(1, customerID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Gets all the appointments from the database
     *
     * @return returns an ObservableList of all appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {


        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("SELECT * FROM appointments as a\n" +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID;");
        ResultSet results = sqlCommand.executeQuery();


        while( results.next() ) {

            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp  lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");


            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );


            allAppointments.add(newAppt);

        }
        sqlCommand.close();
        return allAppointments;

    }

    /**
     * getAppointmentsIn15Mins
     * Gets all appointment queries from the Database to find any existing appointment within the 15 min when signing in.
     *
     * @return retuns Observablelist all user's appointments starting in 15 mins.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentsIn15Mins() throws SQLException {

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime userTZnow = now.atZone(LogonSession.getUserTimeZone());
        ZonedDateTime nowUTC = userTZnow.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime utcPlus15 = nowUTC.plusMinutes(15);


        String rangeStart = nowUTC.format(formatter).toString();
        String rangeEnd = utcPlus15.format(formatter).toString();
        Integer logonUserID = LogonSession.getLoggedOnUser().getUserID();


        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("SELECT * FROM appointments as a " +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE " +
                "Start BETWEEN ? AND ? AND User_ID = ? ");

        sqlCommand.setString(1, rangeStart);
        sqlCommand.setString(2, rangeEnd);
        sqlCommand.setInt(3, logonUserID);

        ResultSet results = sqlCommand.executeQuery();

        while( results.next() ) {

            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );

            allAppointments.add(newAppt);

        }
        return allAppointments;

    }
}
