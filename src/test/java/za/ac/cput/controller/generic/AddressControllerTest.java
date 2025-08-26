package za.ac.cput.controller.generic;

/* MobileGlow Car Wash
   Address Controller Test Class
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
import za.ac.cput.domain.generic.Address;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressControllerTest {

    @LocalServerPort
    private int port;

    private static Address address = new Address.Builder()
            .setStreetNumber("12A")
            .setStreetName("Main Street")
            .setCity("Cape Town")
            .setPostalCode("8000")
            .build();

    private static Address address_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/address";
    }

    @Test
    void a_create() {
        String url = getBaseUrl() + "/create";
        System.out.println("POST URL: " + url);

        ResponseEntity<Address> postResponse = restTemplate.postForEntity(url, address, Address.class);

        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        System.out.println("postResponse: " + postResponse.getBody());

        address_with_Id = postResponse.getBody();
        assertNotNull(address_with_Id);
        assertNotNull(address_with_Id.getAddressID());
        System.out.println("address_with_Id: " + address_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(address_with_Id, "Address is null");
        String url = getBaseUrl() + "/read/" + address_with_Id.getAddressID();
        System.out.println("GET URL: " + url);

        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(address_with_Id.getAddressID(), response.getBody().getAddressID());
        System.out.println("Read response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(address_with_Id, "Address is null");
        String url = getBaseUrl() + "/update/" + address_with_Id.getAddressID();
        System.out.println("PUT URL: " + url);

        Address updatedAddress = new Address.Builder()
                .copy(address_with_Id)
                .setStreetNumber("45B")
                .setStreetName("Updated Street")
                .setCity("Johannesburg")
                .setPostalCode("2000")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Address> requestEntity = new HttpEntity<>(updatedAddress, headers);

        ResponseEntity<Address> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Address.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Address updated = response.getBody();
        assertEquals("45B", updated.getStreetNumber());
        assertEquals("Johannesburg", updated.getCity());
        System.out.println("Updated Address: " + updated);
    }

    @Test
    void d_getAll() {
        String url = getBaseUrl() + "/getAll";
        System.out.println("GET ALL URL: " + url);

        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Addresses (" + response.getBody().length + "):");
        for (Address addr : response.getBody()) {
            System.out.println(addr);
        }
    }

    @Test
    //@Order(5)
    void e_delete() {
        String url = getBaseUrl() + "/delete/" + address_with_Id.getAddressID();
        System.out.println("Deleting address: " + address_with_Id);
        this.restTemplate.delete(url);

        // Verify if the object is deleted
        ResponseEntity<Address> readAddress =
                this.restTemplate.getForEntity(getBaseUrl() + "/read/" + address_with_Id.getAddressID(), Address.class);

        // Expect 404 after deletion
        assertEquals(HttpStatus.NOT_FOUND, readAddress.getStatusCode());
        System.out.println("After deletion, status code: " + readAddress.getStatusCode());
    }


}