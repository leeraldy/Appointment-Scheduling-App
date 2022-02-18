package model;

import utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

/**
 * LoginSession Class: Manages user Login session
 *
 * @author Hussein Coulibaly
 */
public class LoginSession {

    private static User loginUser;
    private static Locale userLocale;
    private static ZoneId userTimeZone;

    public LoginSession() {}

    private static final TimeZone gtmTimeZone = TimeZone.getTimeZone("GMT+0");


    public static boolean accessAttempt(String userNameIn, String userPassword) throws SQLException{
        Connection conn = DBConnection.dbConn();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE " +
                "User_Name = ? AND Password = ?");
        ps.setString(1, userNameIn);
        ps.setString(2, userPassword);
        System.out.println("Executing query...");
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            ps.close();
            return false;

        }
        else {
            loginUser = new User(rs.getString("User_Name"), rs.getInt("User_ID"));
            userLocale = Locale.getDefault();
            userTimeZone = ZoneId.systemDefault();
            ps.close();
            return true;

        }


    }


    public static User getLoginUser() {
        return loginUser;
    }


    public static Locale getUserLocale() {
        return userLocale;

    }


    public static ZoneId getUserTimeZone() {

        return userTimeZone;
    }

  // User signing off
    public static void SignOff() {
        loginUser = null;
        userLocale = null;
        userTimeZone = null;
    }
}
