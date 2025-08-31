package za.ac.cput.controller.generic;

/* MobileGlow Car Wash
   Contact Controller Class
   Author: Inga Zekani (221043756)
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.service.generic.ContactService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contact")
public class ContactController {



    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // CREATE - Add new contact
    @PostMapping("/create")
    public ResponseEntity<Contact> create(@RequestBody Contact contact) {
        Contact created = contactService.create(contact);
        return ResponseEntity.ok(created);
    }

    // READ - Get contact by ID
    @GetMapping("/read/{contactID}")
    public ResponseEntity<Contact> read(@PathVariable Long contactID) {
        Contact contact = contactService.read(contactID);
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    // UPDATE - Update existing contact
    @PutMapping("/update/{contactID}")
    public ResponseEntity<Contact> update(@PathVariable Long contactID, @RequestBody Contact contact) {
        if (!contactID.equals(contact.getContactID())) {
            return ResponseEntity.badRequest().build();
        }

        Contact updated = contactService.update(contact);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // DELETE - Delete contact
    @DeleteMapping("/delete/{contactID}")
    public ResponseEntity<Void> delete(@PathVariable Long contactID) {
        boolean deleted = contactService.delete(contactID);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET ALL - Get all contacts
    @GetMapping("/getAll")
    public ResponseEntity<List<Contact>> getAll() {
        List<Contact> contacts = contactService.getAll();
        return ResponseEntity.ok(contacts);
    }

    // TEST - Simple endpoint to check API
    @GetMapping("/test")
    public String test() {
        return "Contact API is working!";
    }
}
