package model;


/**
 * Customer Class: Manages Customer objects
 *
 * @author Hussein Coulibaly
 */
public class Customer {

    private final Integer ID;
    private final String name;
    private final String country;
    private final String address;
    private final String postalCode;
    private final Integer divisionID;
    private final String division;
    private final String phoneNumber;



    public Customer(Integer inCustomerID, String inName, String inAddress, String inPostalCode,
                    String inPhoneNumber, String inDivision, Integer inDivID, String inCountry) {
        ID = inCustomerID;
        name = inName;
        address = inAddress;
        postalCode = inPostalCode;
        phoneNumber = inPhoneNumber;
        division = inDivision;
        divisionID = inDivID;
        country = inCountry;

    }


    public Integer getCustomerID() {
        return ID;
    }


    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }


    public String getPostalCode() {
        return postalCode;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getDivision() {
        return division;
    }


    public String getCountry() {
        return country;
    }


    public Integer getDivisionID() {
        return divisionID;
    }

//    public static boolean validateCustomer(String name, String address, String postalCode, String phoneNumber ){
//        String errors = " ";
//
//        if(name.isEmpty() || name.isBlank()){
//            errors = errors + " A Name is requires.\n";
//        }
//        if(name.length() > 35){
//            errors = errors + " The name cannot exceed 35 characteres.\n";
//        }
//        if(address.isEmpty() || address.isBlank())
//        {
//            errors = errors + " An entry for Address is required.\n";
//        }
//        if(address.length() > 100)
//        {
//            errors = errors + " The entry for Name cannot exceed 100 characters.\n";
//        }
//        if(postalCode.isEmpty() || postalCode.isBlank())
//        {
//            errors = errors + " An entry for Postal Code is required.\n";
//        }
//        if(postalCode.length() > 50)
//        {
//            errors = errors + " The entry for Postal Code cannot exceed 50 characters.\n";
//        }
//        if(phoneNumber.isEmpty() || phoneNumber.isBlank())
//        {
//            errors = errors + " An entry for Phone is required.\n";
//        }
//        if(phoneNumber.length() > 50)
//        {
//            errors = errors + " The entry for Phone cannot exceed 50 characters.\n";
//        }
//
//        if(errors.isEmpty())
//        {
//            return true;
//        }
//        else
//        {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Customers");
//            alert.setHeaderText("Please verify the following error(s):");
//            alert.setContentText(errors);
//            alert.showAndWait();
//
//            return false;
//        }
//    }

}

