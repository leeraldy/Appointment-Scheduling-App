package utils;

import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LoginSession Class: This class handles user login session by validating username and password
 *
 */

public class LoginSession {


    public static int validAccess(String userName, String password) {


        try
        {
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + password +"'");
            ResultSet rs = ps.executeQuery();


            {
               rs.next();
                if (rs.getString("User_Name").equals(userName))
                {
                    if (rs.getString("Password").equals(password))

                        System.out.println("Executing query...");
                    {
                        return rs.getInt("User_ID");

                    }
                }


            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
}
