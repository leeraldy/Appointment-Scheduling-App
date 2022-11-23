package utils;


import utilities.DBConnection;
import model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * This class handles all country queries in the DB
 * @author Hussein Coulibaly
 */

public class CountryDB {

    /**
     * This method return all countries
     *
     */

    public static ObservableList<Country> getAllCountries() {

        ObservableList<Country> allCountryName = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from countries");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryID, countryName);
                allCountryName.add(country);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return allCountryName;
    }
}
