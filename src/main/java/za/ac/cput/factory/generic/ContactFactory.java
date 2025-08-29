package za.ac.cput.factory.generic;

/* MobileGlow Car Wash
   Contact Factory class
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Contact;
import za.ac.cput.util.Helper;

public class ContactFactory {

    public static Contact createContact(String phoneNumber) {
        // Validate phone number
        if (!Helper.isValidString(phoneNumber) || !isValidPhoneNumber(phoneNumber)) {
            return null;
        }

        return new Contact.Builder()
                .setPhoneNumber(phoneNumber)
                .build();
    }

    public static Contact createContactWithId(Long contactID, String phoneNumber) {
        // Validate phone number
        if (!Helper.isValidString(phoneNumber) || !isValidPhoneNumber(phoneNumber)) {
            return null;
        }

        return new Contact.Builder()
                .setContactID(contactID)
                .setPhoneNumber(phoneNumber)
                .build();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation - adjust regex as needed
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }
}