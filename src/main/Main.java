package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import java.sql.SQLException;

/**
 * Main Class: The application is launches here
 *
 * @author Hussein Coulibaly
 */
public class Main extends Application{

    /**
     * Loads the first scene of the applicartion
     * @param primaryScene sets the primary scene.
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