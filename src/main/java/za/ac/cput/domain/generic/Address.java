package za.ac.cput.domain.generic;

/* MobileGlow Car Wash
   Address
   Author: Inga Zekani (221043756)
 */

public class Address {

    private String addressID;
    private String streetNumber;
    private String streetName;
    private String city;
    private String postalCode;

    //Default Constructors
    public Address() {

    }

    public Address(String addressID, String streetNumber, String streetName, String city, String postalCode) {
        this.addressID = addressID;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getAddressID() {
        return addressID;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
