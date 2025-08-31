package za.ac.cput.domain.generic;

/* MobileGlow Car Wash
   Contact
   Author: Inga Zekani (221043756)
 */

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long contactID;


    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // Default constructor
    public Contact() {}


    // Builder constructor
    private Contact(Builder builder) {
        this.contactID = builder.contactID;
        this.phoneNumber = builder.phoneNumber;
    }

    // Getters
    public Long getContactID() {
        return contactID; }
    public String getPhoneNumber() {
        return phoneNumber; }

    @Override
    public String toString() {
        return "Contact{" +
                "contactID=" + contactID +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    // Builder class
    public static class Builder {
        private Long contactID;
        private String phoneNumber;

        public Builder setContactID(Long contactID) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return phoneNumber.equals(contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }
}
