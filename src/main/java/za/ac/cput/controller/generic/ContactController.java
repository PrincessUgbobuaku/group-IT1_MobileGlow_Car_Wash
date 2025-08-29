package za.ac.cput.controller.generic;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.service.generic.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // CREATE - Add new contact
    @PostMapping("/create")
    public ResponseEntity<?> createContact(@Valid @RequestBody Contact contact, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getFieldError().getDefaultMessage());
        }

        try {
            Contact createdContact = contactService.create(contact);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ - Get contact by ID
    @GetMapping("/read/{contactID}")
    public ResponseEntity<?> read(@PathVariable Long contactID) {
        try {
            Contact contact = contactService.read(contactID);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // UPDATE - Update existing contact
    @PutMapping("/update/{contactID}")
    public ResponseEntity<?> update(@PathVariable Long contactID, @Valid @RequestBody Contact contact, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getFieldError().getDefaultMessage());
        }

        if (!contactID.equals(contact.getContactID())) {
            return ResponseEntity.badRequest().body("ID in path and body do not match");
        }

        try {
            Contact updatedContact = contactService.update(contact);
            return ResponseEntity.ok(updatedContact);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE - Delete contact
    @DeleteMapping("/delete/{contactID}")
    public ResponseEntity<?> deleteContact(@PathVariable Long contactID) {
        try {
            boolean deleted = contactService.delete(contactID);
            return ResponseEntity.ok().body("Contact deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET - Get all contacts
    @GetMapping("/getAll")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAll();
        return ResponseEntity.ok(contacts);
    }

}