package za.ac.cput.factory.generic;

/* MobileGlow Car Wash
   Contact Factory Test
   Author: Inga Zekani (221043756)
 */

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.factory.generic.ContactFactory;

import static org.junit.jupiter.api.Assertions.*;

class ContactFactoryTest {

    @Test
    void testBuildValidContact() {
        Contact contact = ContactFactory.createContactFactory1("0831234567");
        assertNotNull(contact);
        assertEquals("0831234567", contact.getPhoneNumber());

        System.out.println(contact);
    }

    @Test
    void testBuildInvalidContact() {
        Contact contact = ContactFactory.createContactFactory1("");
        assertNull(contact);

        System.out.println(contact);
    }
}
