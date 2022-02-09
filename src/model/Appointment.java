package model;

import java.sql.Timestamp;

/**
 * Appointment Class: Handles Appointments Objects
 *
 * @author Hussein Coulibaly
 */
public class Appointment {

    private Integer appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Timestamp createDate;
    private String createBy;
    private Timestamp lastUpdateDateTime;
    private String lastUpdateBy;
    private Integer customerID;
    private Integer userID;
    private Integer contactID;
    private String contactName;

    /**
     * Appointment constructor for adding appointments
     *
     * @param inputAppointmentID Holds appointment ID (Primary Key).
     * @param inputContactID Holds contact ID (Foreign Key).
     * @param inputContactName Holds Name of contact.
     * @param inputCreateBy Holds user's name who created the appointmen
     * @param inputCreateDate Holds Date appointment was created.
     * @param inputCustomerID Holds customer ID (Foreign Key).
     * @param inputDescription Holds appointment description.
     * @param inputEndDateTime Holds end date/time of appointment.
     * @param inputLastUpdateBy Holds Last person to update the appointment.
     * @param inputLastUpdateDateTime Holds Time/Date of last update.
     * @param inputLocation Holds Appointment Location.
     * @param inputStartDateTime Holds Start Date/time of app
     * @param inputTitle Holds Appointment title
     * @param inputType Holds Appointment Type
     * @param inputUserID Holds User ID(Foreign Key).
     *
     */

    /** Constructor to create list of the appointment /
     *
     * @param inputAppointmentID
     * @param inputTitle
     * @param inputDescription
     * @param inputLocation
     * @param inputType
     * @param inputStartDateTime
     * @param inputEndDateTime
     * @param inputCreateDate
     * @param inputCreateBy
     * @param inputLastUpdateDateTime
     * @param inputLastUpdateBy
     * @param inputCustomerID
     * @param inputUserID
     * @param inputContactID
     * @param inputContactName
     */
    public Appointment(Integer inputAppointmentID, String inputTitle, String inputDescription, String inputLocation,
                       String inputType, Timestamp inputStartDateTime, Timestamp inputEndDateTime,
                       Timestamp inputCreateDate, String inputCreateBy, Timestamp inputLastUpdateDateTime,
                       String inputLastUpdateBy, Integer inputCustomerID, Integer inputUserID, Integer inputContactID,
                       String inputContactName) {

        appointmentID = inputAppointmentID;
        title = inputTitle;
        description = inputDescription;
        location = inputLocation;
        type = inputType;
        startDateTime = inputStartDateTime;
        endDateTime = inputEndDateTime;
        createDate = inputCreateDate;
        createBy = inputCreateBy;
        lastUpdateDateTime = inputLastUpdateDateTime;
        lastUpdateBy = inputLastUpdateBy;
        customerID = inputCustomerID;
        userID = inputUserID;
        contactID = inputContactID;
        contactName = inputContactName;

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
        return  type;
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
