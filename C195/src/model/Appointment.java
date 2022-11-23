package model;

import java.sql.Timestamp;

/**
 * Appointment Class: Manages all Appointments Objects
 *
 * @author Hussein Coulibaly
 */


public class Appointment {

    public int apptID;
    public String title;
    public String description;
    public String location;
    public int contactID;
    public String contactName;
    public String type;
    public Timestamp startTime;
    public Timestamp endTime;
    public int customerID;
    public int userID;



    public Appointment (int apptID, String title, String description, String location, int contactID, String contactName, String type, Timestamp startTime, Timestamp endTime, int customerID, int userID)
    {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.contactName = contactName;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
    }



    public int getApptID() {
        return apptID;
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


    public int getContactId() {
        return contactID;
    }


    public String getContactName() {
        return contactName;
    }


    public String getType() {
        return type;
    }


    public Timestamp getStartTime() {
        return startTime;
    }


    public Timestamp getEndTime() {
        return endTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setAppointmentID(int appointmentID) {
        this.apptID = appointmentID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactID(int contactId) {
        this.contactID = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp end) {
        this.endTime = endTime;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}