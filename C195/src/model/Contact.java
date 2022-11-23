package model;

/**
 * Contact Class: Manages all contact objects
 *
 * @author Hussein Couliibaly
 */

public class Contact {
    public int contactID;
    public String contactName;


    public Contact (int contactID, String contactName)
    {
        this.contactID = contactID;
        this.contactName = contactName;
    }



    public int getContactID() {
        return contactID;
    }


    public String getContactName() {
        return contactName;
    }



    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }



    @Override
    public String toString()
    {
        return (contactName);
    }
}
