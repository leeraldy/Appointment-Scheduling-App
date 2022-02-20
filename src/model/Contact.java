package model;

/**
 * Contact Class: Manages contact objects
 *
 * @author Hussein Couliibaly
 */
public class Contact {

    private Integer contactID;
    private String contactName;

    public Contact(String contactName) {
        this.contactName = contactName;
    }

    private Contact(Integer appointmentID, String contactName){
        contactID = contactID;
        contactName = contactName;
    }

    public Integer getContactID(){
        return this.contactID;
    }

    public String getContactName(){
        return this.contactName;
    }
}

// Contact validation

//public static Integer getContactIDbyName(String contactName){
//    Integer contactID = Integer.parseInt(contactName.substring(0,
//            ":".IndexOf(contactName)));
//    return contactID;
//}

