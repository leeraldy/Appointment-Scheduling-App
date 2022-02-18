package model;

import java.util.Locale;
import java.util.TimeZone;

/**
 * User Class: Manages user objects
 *
 * @author Hussein Coulibaly
 */
public class User {

    // Contructor to create users

    private String userName;
    private int userID;

    public User(String inUserName,int inUserId) {
        userName = inUserName;
        userID = inUserId;

    }

    public String getUserName() {
        return this.userName;
    }

    public Integer getUserID() {
        return this.userID;
    }

}
