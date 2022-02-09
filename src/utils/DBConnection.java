package utils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection Class: Handles SqlDatabase connection
 *
 * @author Hussein Coulibaly
 */
public class DBConnection {

    //
    private static final String  protocol = "jdbc";
    private static String vendor = ":mysql:";
    private static String location = "//localhost/";
    private static String dbName = "client_schedule";
    private static String jdbcUrl = protocol + vendor + location + dbName + "?connectionTimeZone = SERVER"; //LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static String userName = "sqlUser"; //Username
    private static String password = "Passw0rd!"; //Password
    private static Connection cursor;

    /**
     * Manages the database connection
     */
    public DBConnection() { };

    // Driver reference Jdbc

    public static void setJdbcUrl(String jdbcUrlInput) {
        jdbcUrl = jdbcUrlInput;
    }

    // Database name
    public static void setDbName(String dbNameInput) {
        dbName = dbNameInput;
    }


     //username

    public static void setUserName(String userNameInput) {
        userName = userNameInput;
    }

    /**
     * password
     */
    public static void setPassword(String passwordInput) {
        password = passwordInput;
    }

    /**
     * Establishes connection with the database
     */
    public static void connectDB() {
        try {
            Class.forName(driver);
            cursor = DriverManager.getConnection(jdbcUrl, userName, password);

        }
        catch (SQLException error) {
            System.out.println(error.toString() + error.getSQLState());
        }
        catch (ClassNotFoundException error) {
            System.out.println(error.getMessage());
        }

    }


     //@return returns Database object - dbcurso
    public static Connection dbCursor() {
        return cursor;
    }
}