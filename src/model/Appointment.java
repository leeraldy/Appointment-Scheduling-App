package model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This Appointment class takes care of the appointments.
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

    /** Set of all items of the appointment.
     * Appointment constructor
     *
     * @param inputAppointmentID Holds appointment ID (Primary Key).
     * @param inputContactID Holds contact ID (Foreign Key).
     * @param inputContactName Holds Name of contact.
     * @param inputCreateBy Holds Name of user who created the appointment in the DB.
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

    //
    /**
     * getter - Gets the appointment End property.
     * @return ID of the appointment
     */
    public Integer getAppointmentID() {
        return appointmentID;
    }

    /**
     * Getter - Title
     * @return title of the appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter - Description
     * @return description of appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter - Location
     * @return location of appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter - Type
     * @return type of the appointment
     */
    public String getType() {
        return  type;
    }

    /**
     * Getter - Start Date time
     * @return start datetime of appointment
     */
    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    /**
     * Getter - end date time
     * @return end datetime of appointment
     */
    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    /**
     * Getter - create date time
     * @return create date of appointment
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * Getter - created by
     * @return who created appointment
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * Getter - last update date time
     * @return last update of appointment
     */
    public Timestamp getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    /**
     * Getter - last updated by
     * @return datetime of last update
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * getter - customer ID
     * @return customer ID
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Getter - user ID
     * @return user ID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * Getter - contact ID
     * @return contact ID
     */
    public Integer getContactID() {
        return contactID;
    }

    /**
     * Getter - contact name
     * @return name of contact
     */
    public String getContactName() {
        return contactName;
    }
}
