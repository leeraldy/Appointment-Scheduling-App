package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utilities.DBConnection;
import java.sql.*;

/**
 * AppointmentDB Class: Handles all the Appointment queries in Database
 *
 * @author Hussein Coulibaly
 */

public class AppointmentDB {


    /** This method returns all the appointment in the DB
     *
     *
     * @return all appointment saved in the DB
     */

    public static ObservableList<Appointment> getAllAppointments()
    {
        ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID " +
                    "FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID  ORDER BY Appointment_ID");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {

                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");


                Appointment a = new Appointment(appointmentID, title, description, location, contactID, contactName, type, start, end, customerID, userID);
                allAppointmentList.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return allAppointmentList;
    }


    /**
     * This method appointment sorted by weekly
     *
     * @return all weekly appointment occurred in a particular week
     */

    public static ObservableList<Appointment> getWeeklyAppointments()
    {
        ObservableList<Appointment> allWeeklyAppointments = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID AND week(Start, 0) = week(curdate(), 0) ORDER BY Appointment_ID");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointment a = new Appointment(appointmentID, title, description, location, contactID, contactName, type, start, end, customerID, userID);


                allWeeklyAppointments.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return allWeeklyAppointments;
    }

    /**
     * This method returns all appointment sorted by month
     * @return all appointment occurred in a particular month
     */

    public static ObservableList<Appointment> getMonthlyAppointments() {

        ObservableList<Appointment> allMonthlyAppointments = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID AND month(Start) = month(curdate()) ORDER BY Appointment_ID");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointment a = new Appointment(appointmentID, title, description, location, contactID, contactName, type, start, end, customerID, userID);
                allMonthlyAppointments.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return allMonthlyAppointments;
    }

    /**
     * This method allows to add appointments in the DB
     *
     */

    public static void addAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userID, int contactID)
    {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("INSERT INTO appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, NOW(), 'RZ', NOW(), 'RZ', ?, ?, ?)");

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setInt(7, customerId);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);


            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    /**
     *  This method checks for any overlapping appointment and return the appointment found when logging
     */


    public static Boolean checkOverlappedAppointment(Appointment appointment) {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM appointments WHERE ((? <= Start AND ? > Start) OR (? >= Start AND ? < End)) AND Customer_ID = ? AND Appointment_ID <> ?");

            ps.setTimestamp(1, appointment.getStartTime());
            ps.setTimestamp(2, appointment.getEndTime());
            ps.setTimestamp(3, appointment.getStartTime());
            ps.setTimestamp(4, appointment.getStartTime());
            ps.setInt(5, appointment.getCustomerID());
            ps.setInt(6, appointment.getApptID());

            ResultSet rs = ps.executeQuery();


            while (rs.next())
            {

                return true;
            }


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method updates appointments
     *
     */


    public static void modifyAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerID, int userID, int contactID, int appointmentID)
    {
        try {
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?");

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setInt(7, customerID);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);
            ps.setInt(10, appointmentID);

            ps.execute();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * This method deletes appointment in the DB
     */


    public static void deleteAppointment(int appointmentID)
    {
        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE from appointments where Appointment_ID = ?");

            ps.setInt(1, appointmentID);

            ps.execute();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  This method return filtered appointment by type
     */



    public static ObservableList<String> getApptsFilterByType()
    {
        ObservableList<String> allTypeAppointments = FXCollections.observableArrayList();
        try {


            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT DISTINCT type from appointments");


            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                allTypeAppointments.add(rs.getString(1));
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return allTypeAppointments;
    }


    /**
     *  This method returns the total number of appointments from a given time
     */


    public static int getApptByMonthAndTypeCount(String month, String type)
    {
        int total = 0;

        try {


            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT count(*) from appointments WHERE type = ? AND monthname(start) = ?");


            ps.setString(1, type);
            ps.setString(2, month);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return rs.getInt(1);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return total;
    }
}
