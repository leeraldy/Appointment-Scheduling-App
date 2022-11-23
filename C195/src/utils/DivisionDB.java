package utils;


import model.Division;
import utilities.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * DivisionDB Class: Manages all division objects in the DB
 */


public class DivisionDB {

    /**
     * This method returns all divisions in the DB
     * @return divisions
     * @throws SQLException
     */


    public static ObservableList<Division> allDivisions() throws SQLException {

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        try{

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM first_level_divisions");

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {

             int divisionId = rs.getInt("Division_ID");
             String divisionName = rs.getString("Division_Name");
             int countryId = rs.getInt("country_Id");


             Division d = new Division(divisionId, divisionName, countryId);


                divisions.add(d);
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return divisions;
    }

    /**
     * This method returns only US divisions
     */

    public static ObservableList<Division> getUSDivisions()
    {
        ObservableList<Division> USDivisions = FXCollections.observableArrayList();

        try
        {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from first_level_divisions where COUNTRY_ID = 1");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                Division D = new Division(divisionId, divisionName, countryId);
                USDivisions.add(D);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return USDivisions;
    }

    /**
     * This method returns only UK divisions
     */

    public static ObservableList<Division> getUKDivisions()
    {
        ObservableList<Division> UKDivisions = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from first_level_divisions where COUNTRY_ID = 2");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                Division D = new Division(divisionID, divisionName, countryID);
                UKDivisions.add(D);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return UKDivisions;
    }

    /**
     * This method returns only Canada divisions
     */


    public static ObservableList<Division> getCADivisions()
    {
        ObservableList<Division> CADivisions = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from first_level_divisions where COUNTRY_ID = 3");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                Division d = new Division(divisionID, divisionName, countryID);
                CADivisions.add(d);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return CADivisions;
    }


}
