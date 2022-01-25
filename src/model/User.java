package model;

import java.util.Locale;
import java.util.TimeZone;

/**
 * This class handles Users
 *
 * @author Hussein Coulibaly
 */
public class User {
    private String userName;
    private Integer userID;

    /**
     * User Constructor
     *
     * @param inputUserName username
     * @param inputUserId user ID
     */
    public User(String inputUserName,Integer inputUserId) {
        userName = inputUserName;
        userID = inputUserId;

    }

    /**
     * Getter - user name
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter - user ID
     * @return user ID
     */
    public Integer getUserID() {
        return userID;
    }

}
