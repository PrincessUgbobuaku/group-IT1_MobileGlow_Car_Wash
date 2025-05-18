package za.ac.cput.factory.generic;
/* MobileGlow Car Wash
   Factory Contact
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Contact;
import java.util.UUID;
import za.ac.cput.util.Helper;

public class ContactFactory {

    public static Contact createContactFactory1(String phoneNumber) {
        //Create Unique Contact ID
        String contactID = UUID.randomUUID().toString();


        if (!Helper.isValidString(phoneNumber)) {
            return null;
        }

        return new Contact.Builder()
                .setContactID(contactID)
                .setPhoneNumber(phoneNumber)
                .build();
    }
}
