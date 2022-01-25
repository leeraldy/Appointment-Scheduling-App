package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles Users in DB
 *
 * @author Hussein Coulibaly
 */
public class UserDB {

    /**
     * getAllUserID
     * get a list of all user ID's from the DB
     *
     * @return List of all user ID's
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllUserID() throws SQLException {
        ObservableList<Integer> allUserID = FXCollections.observableArrayList();
        PreparedStatement sqlCommand = DBConnection.dbCursor().prepareStatement("SELECT DISTINCT User_ID" +
                " FROM users;");
        ResultSet results = sqlCommand.executeQuery();

        while ( results.next() ) {
            allUserID.add(results.getInt("User_ID"));
        }
        sqlCommand.close();
        return allUserID;
    }
}