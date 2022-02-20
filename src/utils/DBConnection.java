package utils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection Class: Manages SqlDatabase connection
 *
 * @author Hussein Coulibaly
 */
public class DBConnection {

    //
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static String dbName = "client_schedule";
    private static String jdbcUrl = protocol + vendor + location + dbName + "?connectionTimeZone = SERVER"; //LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference

    private static String userName = "sqlUser"; //Username
    private static String password = "Passw0rd!"; //Password
    private static Connection conn;


    public DBConnection() {
    }

    // Driver reference Jdbc

    public static void setJdbcUrl(String jdbcUrlInsert) {
        jdbcUrl = jdbcUrlInsert;
    }

    // Database name
    public static void setDbName(String dbNameInsert) {
        dbName = dbNameInsert;
    }


    //username

    public static void setUserName(String userNameInsert) {
        userName = userNameInsert;
    }


    public static void setPassword(String passwordInsert) {
        password = passwordInsert;
    }

    // Establishes connection with the database

    public static void connectDB() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userName, password);

        } catch (SQLException error) {
            System.out.println(error.toString() + error.getSQLState());
        } catch (ClassNotFoundException error) {
            System.out.println(error.getMessage());
        }

    }


    public static Connection dbConn() {

        return conn;
    }


    // Closing connection
    public static void closedConnection() throws SQLException {
        try {
            conn.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e) {
        }
    }
}
