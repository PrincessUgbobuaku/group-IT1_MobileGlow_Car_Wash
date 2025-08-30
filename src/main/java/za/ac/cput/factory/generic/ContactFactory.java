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
        if ( !isValidPhoneNumber(phoneNumber)) {
            return null;
        }

        return new Contact.Builder()
                .setPhoneNumber(phoneNumber)
                .build();
    }

    
   
}
