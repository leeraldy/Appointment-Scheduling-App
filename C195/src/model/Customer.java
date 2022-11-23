package model;

/**
 * Customer Class: Manages all Customer objects
 *
 * @author Hussein Coulibaly
 */

public class Customer {

    public int customerID;
    public String customerName;
    public String address;
    public String postalCode;
    public String phone;
    public int divisionID;
    public int countryID;
    public String divisionName;



    public Customer (int customerID, String customerName, String address, String postalCode, String phone, int divisionID, int countryID, String divisionName)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.divisionName = divisionName;
    }



    public int getCustomerID() {
        return customerID;
    }


    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getDivisionName() {
        return divisionName;
    }

}
