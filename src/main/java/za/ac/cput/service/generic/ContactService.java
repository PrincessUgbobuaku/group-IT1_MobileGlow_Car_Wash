package za.ac.cput.service.generic;

/* MobileGlow Car Wash
   Contact Service
   Author: Inga Zekani (221043756)
 */

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
        return contactRepository.save(contact);
//        if (contactRepository.existsByPhoneNumber(contact.getPhoneNumber())) {
//            throw new IllegalArgumentException("Contact with phone number " + contact.getPhoneNumber() + " already exists");
//        }
//        return contactRepository.save(contact);
    }

    @Override
    public Contact read(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact with ID " + id + " not found"));
    }

    @Override
    public Contact update(Contact contact) {
        if (!contactRepository.existsById(contact.getContactID())) {
            throw new RuntimeException("Contact with ID " + contact.getContactID() + " not found");
        }
        return contactRepository.save(contact);
    }

    @Override
    public boolean delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact with ID " + id + " not found");
        }
        contactRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }
}
//@Service
//public class ContactService implements IContactService {
//
//    private final IContactRepository contactRepository;
//
//    @Autowired
//    public ContactService(IContactRepository contactRepository) {
//
//        this.contactRepository = contactRepository;
//    }
//
//    @Override
//    public Contact create(Contact contact) {
//        if (contactRepository.existsByPhoneNumber(contact.getPhoneNumber())) {
//            throw new IllegalArgumentException("Contact with phone number " + contact.getPhoneNumber() + " already exists");
//        }
//        return contactRepository.save(contact);
//    }
//
//    @Override
//    public Contact read(Long contactID) {
//        return contactRepository.findById(contactID) // Corrected: findById (not findByID)
//                .orElseThrow(() -> new RuntimeException("Contact with ID " + contactID + " not found"));
//    }
//
//    @Override
//    public Contact update(Contact contact) {
//        // Use existsById() instead of existsByContactID()
//        if (!contactRepository.existsById(contact.getContactID())) {
//            throw new RuntimeException("Contact with ID " + contact.getContactID() + " not found");
//        }
//        return contactRepository.save(contact);
//    }
//
//    @Override
//    public boolean delete(Long contactID) {
//        // Use existsById() instead of existsByContactID()
//        if (!contactRepository.existsById(contactID)) {
//            throw new RuntimeException("Contact with ID " + contactID + " not found");
//        }
//        contactRepository.deleteById(contactID);
//        return true;
//    }
//
//    @Override
//    public List<Contact> getAll() {
//
//        return contactRepository.findAll();
//}

