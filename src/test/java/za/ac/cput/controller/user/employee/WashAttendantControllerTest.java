package za.ac.cput.controller.user.employee;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

    private static Contact contact = ContactFactory.createContact("0725637252");

    private static Address address = AddressFactory.createAddressFactory1("101",
            "Main Street",
            "Cape Town",
            "8000");
    private static Login login = LoginFactory.createLogin("washattendant@gmail.com", "password123");

    private static WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
            "Mike",
            "Johnson",
            User.RoleDescription.EMPLOYEE,
            true,
            "Full-Time",
            true,
            8,
            contact,
            address,
            login);

    private static WashAttendant washAttendantWithId;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/mobileglow/wash-attendants";

    @Test
    void a_create() {
        String url = BASE_URL + "/create";
        ResponseEntity<WashAttendant> response = restTemplate.postForEntity(url, washAttendant, WashAttendant.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
        washAttendantWithId = response.getBody();
        assertNotNull(washAttendantWithId);
        System.out.println("washAttendantWithId: " + washAttendantWithId);
    }

    @Test
    void b_read() {
        assertNotNull(washAttendantWithId, "washAttendantWithId is null");
        String url = BASE_URL + "/read/" + washAttendantWithId.getUserId();
        ResponseEntity<WashAttendant> response = restTemplate.getForEntity(url, WashAttendant.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(washAttendantWithId, "washAttendantWithId is null");
        String url = BASE_URL + "/update";
        WashAttendant updateWashAttendant = new WashAttendant.Builder()
                .copy(washAttendantWithId)
                .setUserName("Michael")
                .setIsFullTime(false)
                .setShiftHours(6)
                .build();
        this.restTemplate.put(url, updateWashAttendant);

        //verify if the object is updated using the read method.
        ResponseEntity<WashAttendant> readWashAttendant =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + updateWashAttendant.getUserId(), WashAttendant.class);
        assertEquals(HttpStatus.OK, readWashAttendant.getStatusCode());

        WashAttendant newWashAttendant = readWashAttendant.getBody();
        assertEquals("Michael", newWashAttendant.getUserName());
        assertFalse(newWashAttendant.getIsFullTime());
        assertEquals(6, newWashAttendant.getShiftHours());
        System.out.println("New wash attendant: " + newWashAttendant);
    }

    @Test
    void d_getAllWashAttendants() {
        String url = BASE_URL + "/getAllWashAttendants";
        ResponseEntity<WashAttendant[]> response = restTemplate.getForEntity(url, WashAttendant[].class);
        assertNotNull(response.getBody());
        System.out.println("response: ");
        for (WashAttendant washAttendant : response.getBody()) {
            System.out.println(washAttendant);
        }
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/delete/" + washAttendantWithId.getUserId();
        System.out.println("Delete wash attendant: " + washAttendantWithId);
        this.restTemplate.delete(url);

        //Verify if the object is deleted.
        ResponseEntity<WashAttendant> readWashAttendant =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + washAttendantWithId.getUserId(), WashAttendant.class);

        //Expect 404 after deletion.
        assertEquals(HttpStatus.NOT_FOUND, readWashAttendant.getStatusCode());
        System.out.println("After deletion, the read wash attendant status code: " + readWashAttendant.getStatusCode());
    }
}
