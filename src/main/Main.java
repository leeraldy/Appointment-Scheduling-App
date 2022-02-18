package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import java.sql.SQLException;

/**
 * Main Class: Manages the application launch
 *
 * @author Hussein Coulibaly
 */
public class Main extends Application{


    @Override
    public void start(Stage primaryScene) throws Exception {
        primaryScene.setTitle("Appointment System");
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        primaryScene.setScene(new Scene(root, 600, 400));
        primaryScene.show();
    }

    public static void main(String[] args) throws SQLException {

        DBConnection.connectDB();
        launch(args);


    }
}