package za.ac.cput.repository.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.repository.generic.ContactRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ContactRepositoryTest {

    private ContactRepository repository;
    private Contact contact;

    @BeforeEach
    void setUp() {
        repository = new ContactRepository();
        contact = ContactFactory.createContactFactory1("0821234567");
        repository.create(contact);
    }

    @Test
    void testCreateAndRead() {
        Contact result = repository.read(contact.getContactID());
        assertNotNull(result);
        assertEquals("0821234567", result.getPhoneNumber());
    }

    @Test
    void testUpdate() {
        Contact updated = new Contact.Builder()
                .copy(contact)
                .setPhoneNumber("0839999999")
                .build();
        repository.update(updated);

        Contact result = repository.read(contact.getContactID());
        assertEquals("0839999999", result.getPhoneNumber());
    }

    @Test
    void testDelete() {
        boolean deleted = repository.delete(contact.getContactID());
        assertTrue(deleted);
        assertNull(repository.read(contact.getContactID()));
    }
}
