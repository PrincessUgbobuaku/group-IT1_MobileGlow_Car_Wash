package za.ac.cput.repository.generic;

import za.ac.cput.domain.generic.Contact;
import za.ac.cput.repository.generic.ContactRepository;
import za.ac.cput.repository.generic.impl.IContactRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository implements IContactRepository {

    private final List<Contact> contactList = new ArrayList<>();

    @Override
    public Contact create(Contact contact) {
        contactList.add(contact);
        return contact;
    }

    @Override
    public Contact read(String contactID) {
        return contactList.stream()
                .filter(c -> c.getContactID().equals(contactID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Contact update(Contact contact) {
        Contact existing = read(contact.getContactID());
        if (existing != null) {
            contactList.remove(existing);
            contactList.add(contact);
            return contact;
        }
        return null;
    }

    @Override
    public boolean delete(String contactID) {
        Contact contact = read(contactID);
        return contact != null && contactList.remove(contact);
    }
}
