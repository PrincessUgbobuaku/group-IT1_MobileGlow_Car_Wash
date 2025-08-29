package za.ac.cput.service.generic;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.util.Helper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    private static Contact testContact;
    private static Contact savedContactId;

    @Test
    void a_testCreateContact() {
        // Use Factory class to create contact - this will test your factory validation
        testContact = ContactFactory.createContact("0728280792");
        assertNotNull(testContact, "Factory should create a valid contact");
        assertNull(testContact.getContactID(), "New contact should not have ID before saving");
        assertEquals("0728280792", testContact.getPhoneNumber());

        Contact created = contactService.create(testContact);
        assertNotNull(created);
        assertNotNull(created.getContactID(), "Saved contact should have an ID");
        assertEquals("0728280792", created.getPhoneNumber());

        savedContactId = created;
        System.out.println("Created contact: " + savedContactId);
    }



    @Test
    void d_testReadContact() {
        assertNotNull(savedContactId, "Contact ID should be set from previous test");

        Contact found = contactService.read(savedContactId.getContactID());
        assertNotNull(found);
        //assertEquals(savedContactId, found.getContactID());
        assertEquals("0728280792", found.getPhoneNumber());
        System.out.println("Read contact: " + found);
    }

    @Test
    void e_testUpdateContact() {
        assertNotNull(savedContactId, "Contact ID should be set from previous test");

        // Read existing contact first
        Contact existingContact = contactService.read(savedContactId.getContactID());
        assertNotNull(existingContact);

        // Create updated contact using the existing contact as base
        Contact updatedContact = new Contact.Builder()
                .copy(existingContact)
                .setPhoneNumber("0728280792") // New phone number
                .build();

        // Verify the updated phone number is valid using factory logic
        assertTrue(Helper.isValidPhoneNumber(updatedContact.getPhoneNumber()),
                "Updated phone number should be valid");

        Contact updated = contactService.update(updatedContact);
        assertNotNull(updated);
        //assertEquals(savedContactId.getContactID(), updated.getContactID());
        assertEquals("0728280792", updated.getPhoneNumber());
        System.out.println("Updated contact: " + updated);

        // Update the test reference
        testContact = updated;
    }

    @Test
    void f_testGetAllContacts() {
        List<Contact> contacts = contactService.getAll();
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        System.out.println("All Contacts (" + contacts.size() + "):");
        for (Contact contact : contacts) {
            System.out.println(contact);
            // Verify each contact has a valid phone number (testing factory validation indirectly)
            assertTrue(Helper.isValidPhoneNumber(contact.getPhoneNumber()),
                    "All contacts should have valid phone numbers");
        }
    }

    @Test
    void g_testDeleteContact() {
        assertNotNull(savedContactId, "Contact ID should be set from previous test");

        // Verify contact exists before deletion
        Contact beforeDelete = contactService.read(savedContactId.getContactID());
        assertNotNull(beforeDelete, "Contact should exist before deletion");

        // Delete the contact
        boolean deleteResult = contactService.delete(savedContactId.getContactID());
        assertTrue(deleteResult, "Delete should return true");

        // Verify contact no longer exists
        Exception exception = assertThrows(RuntimeException.class, () -> {
            contactService.read(savedContactId.getContactID());
        });

        //assertEquals("Contact with ID " + savedContactId + " not found", exception.getMessage());
        System.out.println("Contact deleted successfully. ID: " + savedContactId);
    }

}
