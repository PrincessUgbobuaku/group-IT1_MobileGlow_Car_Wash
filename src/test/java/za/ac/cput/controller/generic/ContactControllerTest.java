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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ContactControllerTest {

    @LocalServerPort
    private int port;


    private static Contact contact = new Contact.Builder()
            .setContactID(1L)
            .setPhoneNumber("+27821234567")
            .build();

    private static Contact contact_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/contacts";
    }

    @Test
    void a_create() {
        String url = getBaseUrl() + "/create";
        System.out.println("POST URL: " + url);

        ResponseEntity<Contact> postResponse = restTemplate.postForEntity(url, contact, Contact.class);

        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        System.out.println("postResponse: " + postResponse.getBody());

        contact_with_Id = postResponse.getBody();
        assertNotNull(contact_with_Id);
        assertNotNull(contact_with_Id.getContactID());
        System.out.println("contact_with_Id: " + contact_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(contact_with_Id, "Contact is null");
        String url = getBaseUrl() + "/read/" + contact_with_Id.getContactID();
        System.out.println("GET URL: " + url);

        ResponseEntity<Contact> response = restTemplate.getForEntity(url, Contact.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(contact_with_Id.getContactID(), response.getBody().getContactID());
        System.out.println("Read response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(contact_with_Id, "Contact is null");
        String url = getBaseUrl() + "/update/" + contact_with_Id.getContactID();
        System.out.println("PUT URL: " + url);

        Contact updatedContact = new Contact.Builder()
                .copy(contact_with_Id)
                .setPhoneNumber("+27829876543")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contact> requestEntity = new HttpEntity<>(updatedContact, headers);

        ResponseEntity<Contact> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Contact.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Contact updated = response.getBody();
        assertEquals("+27829876543", updated.getPhoneNumber());
        System.out.println("Updated Contact: " + updated);
    }

    @Test
    void d_getAll() {
        String url = getBaseUrl() + "/getAll";
        System.out.println("GET ALL URL: " + url);

        ResponseEntity<Contact[]> response = restTemplate.getForEntity(url, Contact[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Contacts (" + response.getBody().length + "):");
        for (Contact cnt : response.getBody()) {
            System.out.println(cnt);
        }
    }

    @Test
    void e_delete() {
        String url = getBaseUrl() + "/delete/" + contact_with_Id.getContactID();
        System.out.println("Deleting contact: " + contact_with_Id);
        this.restTemplate.delete(url);

        // Verify if the object is deleted
        ResponseEntity<Contact> readContact =
                this.restTemplate.getForEntity(getBaseUrl() + "/read/" + contact_with_Id.getContactID(), Contact.class);

        // Expect 404 after deletion
        assertEquals(HttpStatus.NOT_FOUND, readContact.getStatusCode());
        System.out.println("After deletion, status code: " + readContact.getStatusCode());
    }
}