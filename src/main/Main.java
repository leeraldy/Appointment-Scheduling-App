package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.DBConnection;
import java.sql.SQLException;

/**
 * The Main controller of the application.
 *
 * @author Hussein Coulibaly
 */
public class Main extends Application{

    /**
     *
     * This is the call to load the first scene.
     *
     * @param primaryStage sets the primary stage.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Appointment Scheduling");
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     *
     * Start the application.
     * Establishes the database connection
     *
     * @param args arguments for the application.
     *
     * @throws SQLException When unable to connect to the Database.
     */
    public static void main(String[] args) throws SQLException {

        DBConnection.connectDB();
        launch(args);


    }
}