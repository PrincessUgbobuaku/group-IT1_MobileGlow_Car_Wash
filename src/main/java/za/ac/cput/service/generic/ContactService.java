package za.ac.cput.service.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.repository.generic.IContactRepository;
import za.ac.cput.service.IService;

import java.util.List;

@Service
public class ContactService implements IService<Contact, Long> {

    private final IContactRepository contactRepository;

    @Autowired
    public ContactService(IContactRepository contactRepository) {

        this.contactRepository = contactRepository;
    }

    @Override
    public Contact create(Contact contact) {
        if (contactRepository.existsByPhoneNumber(contact.getPhoneNumber())) {
            throw new IllegalArgumentException("Contact with phone number " + contact.getPhoneNumber() + " already exists");
        }
        return contactRepository.save(contact);
    }

    @Override
    public Contact read(Long contactID) {
        return contactRepository.findById(contactID) // Corrected: findById (not findByID)
                .orElseThrow(() -> new RuntimeException("Contact with ID " + contactID + " not found"));
    }

    @Override
    public Contact update(Contact contact) {
        if (!contactRepository.existsByContactID(contact.getContactID())) {
            throw new RuntimeException("Contact with ID " + contact.getContactID() + " not found");
        }
        return contactRepository.save(contact);
    }

    @Override
    public boolean delete(Long contactID) {
        if (!contactRepository.existsByContactID(contactID)) {
            throw new RuntimeException("Contact with ID " + contactID + " not found");
        }
        contactRepository.deleteById(contactID);
        return true;
    }

//    @Override
//    public List<Contact> getAll() {
//        return contactRepository.findAll();
//    }
//
//    @Override
//    public Contact findByPhoneNumber(String phoneNumber) {
//        return contactRepository.findByPhoneNumber(phoneNumber)
//                .orElseThrow(() -> new RuntimeException("Contact with phone number " + phoneNumber + " not found"));
//    }
//
//    @Override
//    public boolean existsByPhoneNumber(String phoneNumber) {
//        return contactRepository.existsByPhoneNumber(phoneNumber);
    }
