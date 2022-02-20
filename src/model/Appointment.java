package model;

import java.sql.Timestamp;

/**
 * Appointment Class: Manages Appointments Objects
 *
 * @author Hussein Coulibaly
 */
public class Appointment {

    private final Integer appointmentID;
    private final String title;
    private final String description;
    private final String location;
    private final String type;
    private final Timestamp startDateTime;
    private final Timestamp endDateTime;
    private final Timestamp createDate;
    private final String createBy;
    private final Timestamp lastUpdateDateTime;
    private final String lastUpdateBy;
    private final int customerID;
    private final int userID;
    private final int contactID;
    private final String contactName;


    public Appointment(Integer inAppointmentID, String inTitle, String inDescription, String inLocation,
                       String inType, Timestamp inStartDateTime, Timestamp inEndDateTime,
                       Timestamp inCreateDate, String inCreateBy, Timestamp inLastUpdateDateTime,
                       String inLastUpdateBy, Integer inCustomerID, Integer inUserID, Integer inContactID,
                       String inContactName) {

        appointmentID = inAppointmentID;
        customerID = inCustomerID;
        contactName = inContactName;
        contactID = inContactID;
        userID = inUserID;
        title = inTitle;
        description = inDescription;
        location = inLocation;
        type = inType;
        startDateTime = inStartDateTime;
        endDateTime = inEndDateTime;
        createDate = inCreateDate;
        createBy = inCreateBy;
        lastUpdateDateTime = inLastUpdateDateTime;
        lastUpdateBy = inLastUpdateBy;



    }


    public Integer getAppointmentID() {
        return appointmentID;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public String getLocation() {
        return location;
    }


    public String getType() {
        return type;
    }


    public Timestamp getStartDateTime() {
        return startDateTime;
    }


    public Timestamp getEndDateTime() {
        return endDateTime;
    }


    public Timestamp getCreateDate() {
        return createDate;
    }


    public String getCreateBy() {
        return createBy;
    }


    public Timestamp getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }


    public String getLastUpdateBy() {
        return lastUpdateBy;
    }


    public Integer getCustomerID() {
        return customerID;
    }


    public Integer getUserID() {
        return userID;
    }


    public Integer getContactID() {
        return contactID;
    }


    public String getContactName() {
        return contactName;
    }
}
