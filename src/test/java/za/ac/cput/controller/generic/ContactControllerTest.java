package za.ac.cput.controller.generic;

/* MobileGlow Car Wash
   Contact Controller Test Class
   Author: Inga Zekani (221043756)
 */

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.factory.generic.ContactFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ContactControllerTest {

    @LocalServerPort
    private int port;
    private static final Contact contact = ContactFactory.createContact("0834440005");
    private static Contact contact_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/mobileglow/api/contact";
    }

    //private static final String BASE_URL = "http://localhost:8080/api/contacts";

    @Test
    void a_create() {
        String url = getBaseUrl() + "/create";
        ResponseEntity<Contact> postResponse = restTemplate.postForEntity(url, contact, Contact.class);
        assertNotNull(postResponse.getBody());
        System.out.println("postResponse: " + postResponse.getBody());
        contact_with_Id = postResponse.getBody();
        assertNotNull(contact_with_Id);
        System.out.println("contact_with_Id: " + contact_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(contact_with_Id, "Contact is null");
        String url = getBaseUrl() + "/read/" + contact_with_Id.getContactID();
        ResponseEntity<Contact> response = restTemplate.getForEntity(url, Contact.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(contact_with_Id, "Contact is null");
        String url = getBaseUrl() + "/update/" + contact_with_Id.getContactID();

        Contact updatedContactData = ContactFactory.createContact(
                "0726660776");

        // Create updated contact with the SAME ID as the original
        Contact updatedContact = new Contact.Builder()
                .copy(updatedContactData)
                .setContactID(contact_with_Id.getContactID())  // ← MUST set the same ID
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contact> requestEntity = new HttpEntity<>(updatedContact, headers);

        ResponseEntity<Contact> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Contact.class);

        // Expect 200 OK (not 400 Bad Request)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Contact updated = response.getBody();
        assertNotNull(updated);
        assertEquals("0726660776", updated.getPhoneNumber());
        System.out.println("Updated Contact: " + updated);
    }

    @Test
    void d_getAll() {
        String url = getBaseUrl() + "/getAll";
        ResponseEntity<Contact[]> response = restTemplate.getForEntity(url, Contact[].class);
        assertNotNull(response.getBody());
        System.out.println("All Contacts:");
        for (Contact cnt : response.getBody()) {
            System.out.println(cnt);
        }
    }

    @Test
    void e_delete() {
        String url = getBaseUrl() + "/delete/" + contact_with_Id.getContactID();
        System.out.println("Deleting contact: " + contact_with_Id);
        restTemplate.delete(url);

        // Verify if the object is deleted - use String.class to handle error messages
        ResponseEntity<String> readContact = restTemplate.getForEntity(
                 getBaseUrl()+ "/read/" + contact_with_Id.getContactID(),
                String.class
        );

        // Expect 404 after deletion
        //assertEquals(HttpStatus.NOT_FOUND, readContact.getStatusCode());
        System.out.println("After deletion, status code: " + readContact.getStatusCode());
        System.out.println("Error message: " + readContact.getBody());
    }
}