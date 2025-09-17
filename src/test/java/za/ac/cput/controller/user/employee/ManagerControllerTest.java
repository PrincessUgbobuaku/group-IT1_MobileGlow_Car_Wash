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
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.ManagerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ManagerControllerTest {

    private static Contact contact = ContactFactory.createContact("0669844409");

    private static Address address = AddressFactory.createAddressFactory1("143",
            "Sir Lowry Street",
            "Cape Town",
            "8000");
    private static Login login = LoginFactory.createLogin("218120192@mycput.ac.za", "student123");

    private static Manager manager = ManagerFactory.createManager(
            "Princess",
            "Voki",
            User.RoleDescription.EMPLOYEE,
            true,
            LocalDate.of(2025, 4, 2),
            "Manager",
            contact,
            address,
            login);

    private static Manager managerWithId;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/mobileglow/Manager";

    @Test
    void a_create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Manager> response = restTemplate.postForEntity(url, manager, Manager.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
        managerWithId = response.getBody();
        assertNotNull(managerWithId);
        System.out.println("managerWithId: " + managerWithId);
    }

    @Test
    void b_read() {
        assertNotNull(managerWithId, "manager_with_Id is null");
        String url = BASE_URL + "/read/" + managerWithId.getUserId();
        ResponseEntity<Manager> response = restTemplate.getForEntity(url, Manager.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(managerWithId, "manager_with_Id is null");
        String url = BASE_URL + "/update";
        Manager updateManager = new Manager.Builder()
                .copy(managerWithId)
                .setUserSurname("Ogbobuaku")
                .build();
        this.restTemplate.put(url, updateManager);

        //verify if the object is updated using the read method.
        ResponseEntity<Manager> readManager =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + updateManager.getUserId(), Manager.class);
        assertEquals(HttpStatus.OK, readManager.getStatusCode());

        Manager newManager = readManager.getBody();
        assertEquals("Ogbobuaku", newManager.getUserSurname());
        System.out.println("New manager: " + newManager);
    }

    @Test
    void d_getAllManagers() {
        String url = BASE_URL + "/getAllManagers";
        ResponseEntity<Manager[]> response = restTemplate.getForEntity(url, Manager[].class);
        assertNotNull(response.getBody());
        System.out.println("response: ");
        for (Manager manager : response.getBody()) {
            System.out.println(manager);
        }
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/delete/" + managerWithId.getUserId();
        System.out.println("Delete manager: " + managerWithId);
        this.restTemplate.delete(url);

        //Verify if the object is deleted.
        ResponseEntity<Manager> readManager =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + managerWithId.getUserId(), Manager.class);

        //Expect 404 after deletion.
        assertEquals(HttpStatus.NOT_FOUND, readManager.getStatusCode());
        System.out.println("After deletion, the read manager status code: " + readManager.getStatusCode());
    }
}