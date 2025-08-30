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
import org.springframework.http.*;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.factory.generic.AddressFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressControllerTest {


    private static Address address = AddressFactory.createAddressFactory1(
            "105",
            "Sir Lowry",
            "Cape Town",
            "7100");
    private static Address address_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/api/address";


    @Test
    void a_create() {
        assertNotNull(address, "Factory should create a valid address");
        String url = BASE_URL + "/create";

        ResponseEntity<Address> postResponse = restTemplate.postForEntity(url, address, Address.class);
        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        address_with_Id = postResponse.getBody();
        assertNotNull(address_with_Id.getAddressID());
        System.out.println("Created Address: " + address_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(address_with_Id, "Address is null");
        String url = BASE_URL + "/read/" + address_with_Id.getAddressID();

        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Read Address: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(address_with_Id, "Address is null");
        String url = BASE_URL + "/update/" + address_with_Id.getAddressID();

        // Use factory to create updated address data
        Address updatedAddressData = AddressFactory.createAddressFactory1(
                "1003",
                "Edna Street",
                "Durban",
                "4000");

        assertNotNull(updatedAddressData, "Factory should create updated address");

        // Set the same ID for update
        Address updatedAddress = new Address.Builder()
                .copy(updatedAddressData)
                .setAddressID(address_with_Id.getAddressID())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Address> requestEntity = new HttpEntity<>(updatedAddress, headers);

        ResponseEntity<Address> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Address.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Address updated = response.getBody();
        assertEquals("1003", updated.getStreetNumber());
        assertEquals("Durban", updated.getCity());
        System.out.println("Updated Address: " + updated);
    }

    @Test
    void d_getAll() {
        String url = BASE_URL + "/getAll";
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("All Addresses (" + response.getBody().length + "):");
        for (Address addr : response.getBody()) {
            System.out.println(addr);
        }
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/delete/" + address_with_Id.getAddressID();
        restTemplate.delete(url);

        // Use String.class to handle error messages
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + "/read/" + address_with_Id.getAddressID(),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Address deleted successfully: " + response.getBody());
    }
}
