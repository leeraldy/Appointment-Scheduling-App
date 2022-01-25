package utility;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * SqlDatabase
 *
 * @author Hussein Coulibaly
 */
public class DBConnection {

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
     * Handles the connection to the database
     */
    public DBConnection() { };

    /**
     * Setter - JDBC url
     *
     * @param jdbcUrlInput JDBC url for connectivity
     */
    public static void setJdbcUrl(String jdbcUrlInput) {
        jdbcUrl = jdbcUrlInput;
    }

    /**
     * Setter- DbName
     *
     * @param dbNameInput DB name for connectivity
     */
    public static void setDbName(String dbNameInput) {
        dbName = dbNameInput;
    }

    /**
     * Setter - DB username
     *
     * @param userNameInput DB username for DB login
     */
    public static void setUserName(String userNameInput) {
        userName = userNameInput;
    }

    /**
     * Setter - DB password
     *
     * @param passwordInput password for login
     */
    public static void setPassword(String passwordInput) {
        password = passwordInput;
    }

    /**
     * connectDB
     * Connect to the database
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

    /**
     * dbCursor
     *
     * @return DB connection for use
     */
    public static Connection dbCursor() {
        return cursor;
    }
}