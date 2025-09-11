package za.ac.cput.controller.user.employee;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.WashAttendantFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class WashAttendantControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "/api/wash-attendants";

    private static Contact contact = ContactFactory.createContactFactory1("0725637252");
    private static Address address = AddressFactory.createAddressFactory1("101", "Main Street", "Cape Town", "8000");
    private static Login login = LoginFactory.createLogin("washattendant@gmail.com", "password123");

    private static WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
            "Mike",
            "Johnson",
            User.RoleDescription.WASH_ATTENDANT,
            true,
            "Full-Time",
            true,
            8,
            contact,
            address,
            login);

    private static WashAttendant createdWashAttendant;

    @Test
    void a_create() {
        String url = BASE_URL;
        ResponseEntity<WashAttendant> response = restTemplate.postForEntity(url, washAttendant, WashAttendant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        createdWashAttendant = response.getBody();
        assertNotNull(createdWashAttendant.getUserId());
        assertEquals(washAttendant.getUserName(), createdWashAttendant.getUserName());

        System.out.println("Created WashAttendant: " + createdWashAttendant);
    }

    @Test
    void b_read() {
        String url = BASE_URL + "/" + createdWashAttendant.getUserId();
        ResponseEntity<WashAttendant> response = restTemplate.getForEntity(url, WashAttendant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdWashAttendant.getUserId(), response.getBody().getUserId());

        System.out.println("Read WashAttendant: " + response.getBody());
    }

    @Test
    void c_update() {
        WashAttendant updatedWashAttendant = new WashAttendant.Builder()
                .copy(createdWashAttendant)
                .setUserName("Michael")
                .setIsFullTime(false)
                .setShiftHours(6)
                .build();

        String url = BASE_URL + "/" + createdWashAttendant.getUserId();
        HttpEntity<WashAttendant> requestEntity = new HttpEntity<>(updatedWashAttendant);
        ResponseEntity<WashAttendant> response = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, WashAttendant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Michael", response.getBody().getUserName());
        assertFalse(response.getBody().getIsFullTime());
        assertEquals(6, response.getBody().getShiftHours());

        System.out.println("Updated WashAttendant: " + response.getBody());
    }

    @Test
    void d_getAll() {
        String url = BASE_URL;
        ResponseEntity<WashAttendant[]> response = restTemplate.getForEntity(url, WashAttendant[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        System.out.println("All WashAttendants: " + response.getBody().length);
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/" + createdWashAttendant.getUserId();
        restTemplate.delete(url);

        // Verify deletion
        ResponseEntity<WashAttendant> response = restTemplate.getForEntity(url, WashAttendant.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        System.out.println("Deleted WashAttendant successfully: " + createdWashAttendant.getUserId());
    }
}