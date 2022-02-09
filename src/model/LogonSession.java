package model;

import utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

/**
 * LogonSession Class: Handles user Login session
 *
 * @author Hussein Coulibaly
 */
public class LogonSession {

    private static User loggedOnUser;
    private static Locale userLocale;
    private static ZoneId userTimeZone;

    /**
     * Constructor to initiate LogonSession
     */
    public LogonSession() {}

    private static final TimeZone gtmTimeZone = TimeZone.getTimeZone("GMT+0");

    /**
     * Login attempts by verifying the username and password
     *
     * @param userNameInput user input username
     * @param userPassword user input password
     *
     * @return Boolean displaying a successful loin
     * @throws SQLException
     */
    public static boolean attemptLogon(String userNameInput, String userPassword) throws SQLException{
        Connection conn = DBConnection.dbCursor();
        PreparedStatement sqlCommand = conn.prepareStatement("SELECT * FROM users WHERE " +
                "User_Name = ? AND Password = ?");
        sqlCommand.setString(1, userNameInput);
        sqlCommand.setString(2, userPassword);
        System.out.println("Executing query...");
        ResultSet result = sqlCommand.executeQuery();
        if (!result.next()) {
            sqlCommand.close();
            return false;

        }
        else {
            loggedOnUser = new User(result.getString("User_Name"), result.getInt("User_ID"));
            userLocale = Locale.getDefault();
            userTimeZone = ZoneId.systemDefault();
            sqlCommand.close();
            return true;

        }


    }


    public static User getLoggedOnUser() {
        return loggedOnUser;
    }


    public static Locale getUserLocale() {
        return userLocale;

    }


    public static ZoneId getUserTimeZone() {
        return userTimeZone;
    }

    /**
     * Sign off user from the application
     */
    public static void logOff() {
        loggedOnUser = null;
        userLocale = null;
        userTimeZone = null;
    }
}
