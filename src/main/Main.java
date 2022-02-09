package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.DBConnection;
import java.sql.SQLException;

/**
 * Main Class: The application is launches here
 *
 * @author Hussein Coulibaly
 */
public class Main extends Application{

    /**
     *
     * This is the call to load the first scene.
     * @param primaryScene sets the primary stage.
     */

    @Override
    public void start(Stage primaryScene) throws Exception {
        primaryScene.setTitle("Appointment Scheduling");
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        primaryScene.setScene(new Scene(root));
        primaryScene.show();
    }

    public static void main(String[] args) throws SQLException {

        DBConnection.connectDB();
        launch(args);


    }
}