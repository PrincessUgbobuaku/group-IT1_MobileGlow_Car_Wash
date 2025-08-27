package za.ac.cput.service.generic;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.generic.Contact;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    private static Contact testContact;
    private static Long savedContactId;

    @Test
    void a_testCreateContact() {
        Contact contact = new Contact.Builder()
                .setPhoneNumber("+27616784231")
                .build();

        Contact saved = contactService.create(contact);
        assertNotNull(saved.getContactID());
        assertEquals("+27616784231", saved.getPhoneNumber());
        System.out.println("Created Contact: " + saved);

        testContact = saved;
        savedContactId = saved.getContactID();
    }


    @Test
    void c_testReadContact() {
        assertNotNull(savedContactId, "Contact ID is null - create test might have failed");

        Contact found = contactService.read(savedContactId);
        assertNotNull(found);
        assertEquals(savedContactId, found.getContactID());
        assertEquals("+27616784231", found.getPhoneNumber());
        System.out.println("Read Contact: " + found);
    }

    @Test
    void d_testUpdateContact() {
        assertNotNull(testContact, "Test contact is null - create test might have failed");

        Contact updatedContact = new Contact.Builder()
                .copy(testContact)
                .setPhoneNumber("+27616784231")
                .build();

        Contact updated = contactService.update(updatedContact);
        assertNotNull(updated);
        assertEquals(testContact.getContactID(), updated.getContactID());
        assertEquals("+27616784231", updated.getPhoneNumber());
        System.out.println("Updated Contact: " + updated);

        testContact = updated; // Update the reference for later tests
    }


    @Test
    void f_testGetAllContacts() {
        List<Contact> contacts = contactService.getAll();
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());
        assertTrue(contacts.size() >= 1);

        System.out.println("All Contacts (" + contacts.size() + "):");
        for (Contact contact : contacts) {
            System.out.println(contact);
            // Verify that our test contact is in the list
            if (contact.getContactID().equals(savedContactId)) {
                assertEquals("+27616784231", contact.getPhoneNumber());
            }
        }
    }

    @Test
    void g_testDeleteContact() {
        assertNotNull(savedContactId, "Contact ID is null - create test might have failed");

        // First verify the contact exists
        Contact beforeDelete = contactService.read(savedContactId);
        assertNotNull(beforeDelete, "Contact should exist before deletion");

        // Delete the contact
        boolean deleteResult = contactService.delete(savedContactId);
        assertTrue(deleteResult, "Delete operation should return true");

        // Verify the contact no longer exists
        Exception exception = assertThrows(RuntimeException.class, () -> {
            contactService.read(savedContactId);
        });

        assertEquals("Contact with ID " + savedContactId + " not found", exception.getMessage());
        System.out.println("Contact deleted successfully. ID: " + savedContactId);
    }

}