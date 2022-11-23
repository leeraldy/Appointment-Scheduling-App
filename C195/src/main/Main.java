package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DBConnection;
import java.sql.*;

/**
 * Main Class: Manages the application launch
 *
 * @author Hussein Coulibaly
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("Appointment Scheduling System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * This method handles DB connection
     * @param args passed arguments to launch connection
     * @throws SQLException returns an error if found
     */
    public static void main(String[] args) throws SQLException {

        DBConnection.openConnection();

        launch(args);

        DBConnection.closedConnection();

    }


}