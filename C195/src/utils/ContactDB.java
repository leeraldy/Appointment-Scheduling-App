package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class handles all contacts details queries
 *
 * @author Hussein Coulibaly
 */


public class ContactDB {

    /**
     * This method returns all contacts
     * @return all contacts saved in DB
     */

    public static ObservableList<Contact> getAllContacts()
    {

        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from contacts");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Contact c = new Contact(contactID, contactName);
                allContacts.add(c);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return allContacts;
    }

    /**
     * This method returns all contacts by name
     * @return contacts name
     * @throws SQLException
     */

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
}
