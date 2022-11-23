package model;

/**
 * Division Class: Manages all Division objects
 *
 * @author Hussein Coulibaly
 */

public class Division {
    private int divisionID;
    private String divisionName;
    private int countryID;



    public Division (int divisionID, String divisionName, int countryID)
    {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }


    public int getDivisionID() {
        return divisionID;
    }


    public String getName() {
        return divisionName;
    }


    public int getCountryID() {
        return countryID;
    }


    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }


    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }


    public void setCountryId(int countryId) {
        this.countryID = countryId;
    }


    @Override
    public String toString()
    {
        return (divisionName);
    }

}
