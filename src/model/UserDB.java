package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDB Class: Manages users in the Database
 *
 * @author Hussein Coulibaly
 */
public class UserDB {


    public static ObservableList<Integer> getAllUserID() throws SQLException {
        ObservableList<Integer> allUserID = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT DISTINCT User_ID" +
                " FROM users;");
        ResultSet rs = ps.executeQuery();

        while ( rs.next() ) {
            allUserID.add(rs.getInt("User_ID"));
        }
        ps.close();
        return allUserID;
    }
}