package model;

import java.sql.Timestamp;

/**
 * Customer Class: Handles Customer objects
 *
 * @author Hussein Coulibaly
 */
public class Customer {

    private Integer ID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private Integer divisionID;
    private String division;
    private String country;

    /**
     * Constructor to create new Customer object
     *
     * @param inputCustomerID Customer ID
     * @param inputName Customer name
     * @param inputAddress Customer address
     * @param inputPostalCode Customer postal code
     * @param inputPhoneNumber Customer phone number
     * @param inputDivision Customer first division
     * @param inputDivID Customer Division
     * @param inputCountry Customer Country
     */
    public Customer(Integer inputCustomerID, String inputName, String inputAddress, String inputPostalCode,
                    String inputPhoneNumber, String inputDivision, Integer inputDivID, String inputCountry) {
        ID = inputCustomerID;
        name = inputName;
        address = inputAddress;
        postalCode = inputPostalCode;
        phoneNumber = inputPhoneNumber;
        division = inputDivision;
        divisionID = inputDivID;
        country = inputCountry;

    }


    // Getters

    /**
     * Getter - customer ID
     * @return customer ID
     */
    public Integer getCustomerID() {
        return ID;
    }

    /**
     * Getter - name
     * @return Customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter - Address
     * @return Customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter - postal code
     * @return Customer postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter - Phone number
     * @return phone number of customer
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter - Division
     * @return Customers division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Getter - Country
     * @return Customer Country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Getter - Division ID
     * @return Customer division
     */
    public Integer getDivisionID() {
        return divisionID;
    }
}
