package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection Class: Manages Database connection with the application
 *
 * @author  Hussein Coulibaly
 *
 */


public class DBConnection {

    private static String protocol = "jdbc";
    private static String vendor = ":mysql:";
    private static String location = "//localhost/";
    private static String dbName = "client_schedule";
    private static String jdbcURL = protocol + vendor + location + dbName + "?connectionTimeZone = SERVER"; //LOCAL
    private static String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static Connection conn = null;
    private static String username = "sqlUser"; //Username
    private static String password = "Passw0rd!"; //Password


    /**
     * This method creates connection with the DB
     * @return authorized connection
     */

    public static Connection openConnection() {
        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successfully established");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return conn;

    }

    /**
     * This method ensures connection to DB is established
     * @return success connection
     */
    public static Connection dbConn()
    {
        return conn;
    }

    /**
     * This method manages to end DB connection
     */

    public static void closedConnection()
    {
        try
        {
            conn.close();
            System.out.println("Connection terminated!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

}