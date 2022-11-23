package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDB class: This class handles all users objects in the DB
 *
 */

public class UserDB {


    public static ObservableList<User> getAllUsers()
    {

        ObservableList<User> allUsers = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from users");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User user = new User(userID, userName, password);
                allUsers.add(user);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return allUsers;
    }

}
