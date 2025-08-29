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
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.AccountantFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AccountantControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "/api/accountants";

    private static Contact contact = ContactFactory.createContactFactory1("0725637252");
    private static Address address = AddressFactory.createAddressFactory1("101", "Main Street", "Cape Town", "8000");
    private static Login login = LoginFactory.createLogin("accountant@gmail.com", "password123");

    private static Accountant accountant = AccountantFactory.createAccountant(
            "John",
            "Doe",
            User.RoleDescription.ACCOUNTANT,
            true,
            "Full-Time",
            true,
            contact,
            address,
            login);

    private static Accountant createdAccountant;

    @Test
    void a_create() {
        String url = BASE_URL;
        ResponseEntity<Accountant> response = restTemplate.postForEntity(url, accountant, Accountant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        createdAccountant = response.getBody();
        assertNotNull(createdAccountant.getUserId());
        assertEquals(accountant.getUserName(), createdAccountant.getUserName());

        System.out.println("Created Accountant: " + createdAccountant);
    }

    @Test
    void b_read() {
        String url = BASE_URL + "/" + createdAccountant.getUserId();
        ResponseEntity<Accountant> response = restTemplate.getForEntity(url, Accountant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdAccountant.getUserId(), response.getBody().getUserId());

        System.out.println("Read Accountant: " + response.getBody());
    }

    @Test
    void c_update() {
        Accountant updatedAccountant = new Accountant.Builder()
                .copy(createdAccountant)
                .setUserName("Jonathan")
                .setHasTaxFillingAuthority(false)
                .build();

        String url = BASE_URL + "/" + createdAccountant.getUserId();
        HttpEntity<Accountant> requestEntity = new HttpEntity<>(updatedAccountant);
        ResponseEntity<Accountant> response = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, Accountant.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jonathan", response.getBody().getUserName());
        assertFalse(response.getBody().getHasTaxFillingAuthority());

        System.out.println("Updated Accountant: " + response.getBody());
    }

    @Test
    void d_getAll() {
        String url = BASE_URL;
        ResponseEntity<Accountant[]> response = restTemplate.getForEntity(url, Accountant[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        System.out.println("All Accountants: " + response.getBody().length);
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/" + createdAccountant.getUserId();
        restTemplate.delete(url);

        // Verify deletion
        ResponseEntity<Accountant> response = restTemplate.getForEntity(url, Accountant.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        System.out.println("Deleted Accountant successfully: " + createdAccountant.getUserId());
    }
}
