package model;


/**
 * User Class: Manages all User objects
 *
 * @author Hussein Coulibaly
 */

public class User {
    public int userID;
    public String userName;
    public String password;


    public User (int userID, String userName, String password)
    {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }


    public int getUserID() {
        return userID;
    }


    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString()
    {
        return "[" + userID + "] " + userName;
    }
}
