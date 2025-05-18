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

    public Contact(Builder builder) {
        this.contactID = builder.contactID;
        this.phoneNumber = builder.phoneNumber;
    }


    public String getContactID() {
        return contactID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactID='" + contactID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public static class Builder {

        private String contactID;
        private String phoneNumber;

        public Builder setContactID(String contactID) {
            this.contactID = contactID;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Builder copy(Contact contact) {
            this.contactID = contact.contactID;
            this.phoneNumber = contact.phoneNumber;
            return this;
        }
        public Contact build() {
            return new Contact(this);
        }
    }
}