package za.ac.cput.service.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.repository.generic.IContactRepository;

import java.util.List;

@Service
public class ContactService implements IContactService {

    private final IContactRepository contactRepository;

    @Autowired
    public ContactService(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact create(Contact contact) {
        // Check if phone number already exists using findAll() and stream
        boolean phoneNumberExists = contactRepository.findAll().stream()
                .anyMatch(c -> c.getPhoneNumber().equals(contact.getPhoneNumber()));

        if (phoneNumberExists) {
            throw new IllegalArgumentException("Contact with phone number " + contact.getPhoneNumber() + " already exists");
        }
        return contactRepository.save(contact);
    }

    @Override
    public Contact read(Long contactID) {
        return contactRepository.findById(contactID)
                .orElseThrow(() -> new RuntimeException("Contact with ID " + contactID + " not found"));
    }

    @Override
    public Contact update(Contact contact) {
        // Check if contact exists using existsById() (built-in method)
        if (!contactRepository.existsById(contact.getContactID())) {
            throw new RuntimeException("Contact with ID " + contact.getContactID() + " not found");
        }
        return contactRepository.save(contact);
    }

    @Override
    public boolean delete(Long contactID) {
        // Check if contact exists using existsById() (built-in method)
        if (!contactRepository.existsById(contactID)) {
            throw new RuntimeException("Contact with ID " + contactID + " not found");
        }
        contactRepository.deleteById(contactID);
        return true;
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }
}
