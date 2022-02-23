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
        contactName = contactName;
    }

    private Contact(Integer appointmentID, String contactName){
        contactID = contactID;
        contactName = contactName;
    }

    public Integer getContactID(){
        return contactID;
    }

    public String getContactName(){
        return contactName;
    }
}

// Contact validation

//public static Integer getContactIDbyName(String contactName){
//    Integer contactID = Integer.parseInt(contactName.substring(0,
//            ":".IndexOf(contactName)));
//    return contactID;
//}

