package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AppointmentDB Class: Manages all the SQL queries in Database
 *
 * @author Hussein Coulibaly
 */
public class AppointmentDB {

    // Pulls all the appointments in the database

    public static ObservableList<Appointment> getAllDateFilteredAppointmentsView(ZonedDateTime startSpan, ZonedDateTime endSpan)
            throws SQLException {

        ObservableList<Appointment> filteredApptsList = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                //"SELECT * FROM appointmemnts WHERE Contact_ID BETWEEN" + "start in ? and ?"
                "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE" +
                        " Start between ? AND ?"
        );

        String startSpanString = startSpan.format(formatter);
        String endSpanString = endSpan.format(formatter);

        ps.setString(1, startSpanString);
        ps.setString(2, endSpanString);

        ResultSet rs = ps.executeQuery();

        while( rs.next() ) {

            int apptID = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDescription = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptType = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
            String apptCreateBy = rs.getString("Create_by");
            Timestamp apptLastUpdateDateTime = rs.getTimestamp("Last_Update");
            String apptLastUpdateBy = rs.getString("Last_Update_By");
            int apptCustomerID = rs.getInt("Customer_ID");
            int apptUserID = rs.getInt("User_ID");
            int apptContactID = rs.getInt("Contact_ID");
            String apptContactName = rs.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    apptID, apptTitle, apptDescription, apptLocation, apptType, start, end, apptCreateDate,
                    apptCreateBy, apptLastUpdateDateTime, apptLastUpdateBy, apptCustomerID, apptUserID, apptContactID, apptContactName
            );
            filteredApptsList.add(newAppt);
        }


        ps.close();
        return filteredApptsList;

    }


    public static ObservableList<String> monthlyReviewTypeAndMonth() throws SQLException {
        ObservableList<String> reviewStrings = FXCollections.observableArrayList();

        reviewStrings.add("The Total Number of Appointments by type and month:\n");

        PreparedStatement typePs = DBConnection.dbConn().prepareStatement(
                "SELECT Type, COUNT(Type) as \"Total\" FROM appointments GROUP BY Type");

        PreparedStatement monthPs = DBConnection.dbConn().prepareStatement(
                "SELECT MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month");

        ResultSet typeResults = typePs.executeQuery();
        ResultSet monthResults = monthPs.executeQuery();

        while (typeResults.next()) {
            String typeStr = "Type: " + typeResults.getString("Type") + " Count: " +
                    typeResults.getString("Total") + "\n";
            reviewStrings.add(typeStr);

        }

        while (monthResults.next()) {
            String monthStr = "Month: " + monthResults.getString("Month") + " Count: " +
                    monthResults.getString("Total") + "\n";
            reviewStrings.add(monthStr);

        }

        monthPs.close();
        typePs.close();

        return reviewStrings;

    }


    public static ObservableList<Appointment> getAppointmentsFilteredByCustomer(
            LocalDate apptDate, Integer inputCustomerID) throws SQLException {

        ObservableList<Appointment> filteredApptsList = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement(
                "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c " +
                        "ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0 AND Customer_ID = ?;"
        );

        ps.setInt(2, inputCustomerID);

        ps.setString(1, apptDate.toString());

        ResultSet rs = ps.executeQuery();

        while( rs.next() ) {

            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            Timestamp createdDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_by");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );
            filteredApptsList.add(newAppt);
        }

        ps.close();
        return filteredApptsList;

    }


    public static Boolean updateAppointment(Integer inApptID, String inTitle, String inDescription,
                                            String inLocation, String inType, ZonedDateTime inputStart,
                                            ZonedDateTime inEnd, String inLastUpdateBy, Integer inCustomerID,
                                            Integer inUserID, Integer inContactID) throws SQLException {

        PreparedStatement ps = DBConnection.dbConn().prepareStatement("UPDATE appointments "
                + "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?,Last_Updated_By=?, " +
                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inStartString = inputStart.format(formatter);
        String inEndString = inEnd.format(formatter);

        ps.setString(1,inTitle);
        ps.setString(2, inDescription);
        ps.setString(3, inLocation);
        ps.setString(4, inType);
        ps.setString(5, inStartString);
        ps.setString(6, inEndString);
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(8, inLastUpdateBy);
        ps.setInt(9, inCustomerID);
        ps.setInt(10, inUserID);
        ps.setInt(11, inContactID);
        ps.setInt(12, inApptID);

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


    public static Boolean addAppointment(String inTitle, String inDescription,
                                         String inLocation, String inType, ZonedDateTime inStart,
                                         ZonedDateTime inEnd, String inCreatedBy,
                                         String inLastUpdateBy, Integer inCustomerID,
                                         Integer inUserID, Integer inContactID) throws SQLException {

        PreparedStatement ps = DBConnection.dbConn().prepareStatement("INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Create_date, \n" +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStartString = inStart.format(formatter);
        String inputEndString = inEnd.format(formatter);


        ps.setString(1, inTitle);
        ps.setString(2, inDescription);
        ps.setString(3, inLocation);
        ps.setString(4, inType);
        ps.setString(5, inputStartString);
        ps.setString(6, inputEndString);
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(8, inCreatedBy);
        ps.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(10, inLastUpdateBy);
        ps.setInt(11, inCustomerID);
        ps.setInt(12, inUserID);
        ps.setInt(13, inContactID);


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


    public static Boolean removeAppointment(Integer inputApptID) throws SQLException {

        PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE FROM appointments " +
                "WHERE Appointment_ID = ?");

        ps.setInt(1, inputApptID);

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


    public static Boolean cancelCustomersAppointments(Integer customerID) throws SQLException {

        PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE FROM appointments " +
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


    public static ObservableList<Appointment> getAllAppointments() throws SQLException {


        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM appointments as a\n" +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID;");
        ResultSet rs = ps.executeQuery();


        while( rs.next() ) {

            Integer appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            Timestamp createdDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_by");
            Timestamp  lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Integer customerID = rs.getInt("Customer_ID");
            Integer userID = rs.getInt("User_ID");
            Integer contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");


            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );


            allAppointments.add(newAppt);

        }
        ps.close();
        return allAppointments;

    }


    public static ObservableList<Appointment> getCheckAppointmentsIn15Mins() throws SQLException {

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime userTZnow = now.atZone(LoginSession.getUserTimeZone());
        ZonedDateTime nowUTC = userTZnow.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime utcPlus15 = nowUTC.plusMinutes(15);


        String spanStart = nowUTC.format(formatter);
        String spanEnd = utcPlus15.format(formatter);
        Integer loginUserID = LoginSession.getLoginUser().getUserID();


        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM appointments as a " +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE " +
                "Start BETWEEN ? AND ? AND User_ID = ? ");

        ps.setString(1, spanStart);
        ps.setString(2, spanEnd);
        ps.setInt(3, loginUserID);

        ResultSet rs = ps.executeQuery();

        while( rs.next() ) {
            //if(rs.getTimestamp("Start").before(Timestamp.valueOf(LocalDateTime.now().plusMinutes(15))))
            //                {
            //                    Appointment CheckAppointmentsIn15Mins = new Appointment(rs.getInt("Appointment_ID"),
            //                            rs.getDate("date(start)"), rs.getTime("time(start)"));
            //                    apptCounter++;
            //                    CheckAppointmentsIn15Mins.add(CheckAppointmentsIn15Mins)

            Integer appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            Timestamp createdDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_by");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Integer customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );

            allAppointments.add(newAppt);

        }
        return allAppointments;

    }
}
