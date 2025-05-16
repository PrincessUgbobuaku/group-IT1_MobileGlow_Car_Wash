package za.ac.cput.domain.generic;

/* MobileGlow Car Wash
   Contact class
   Author: Inga Zekani (221043756)
 */

public class Contact {


    private String contactID;
    private String phoneNumber;

    //Default Constructor
    public Contact() {

    }

    public Contact(String contactID, String phoneNumber) {
        this.contactID = contactID;
        this.phoneNumber = phoneNumber;
    }


    public String getContactID() {
        return contactID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}