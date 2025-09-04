package za.ac.cput.controller.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.user.Login;
import za.ac.cput.factory.user.LoginFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class LoginControllerTest {

    private static Login login = LoginFactory.createLogin(
            "Twalo@yahoo.com",
            "Broker2001"
    );

    private static Login login_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/mobileglow/Login";

    @Test
    void a_create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Login> postResponse = restTemplate.postForEntity(url, login, Login.class);

        System.out.println("Status: " + postResponse.getStatusCode());
        System.out.println("Body: " + postResponse);

        assertNotNull(postResponse.getBody());
        System.out.println("postResponse: " + postResponse.getBody());
        login_with_Id = postResponse.getBody();
        assertNotNull(login_with_Id);
        System.out.println("login_with_Id: " + login_with_Id);

    }

    @Test
    void b_read() {
        assertNotNull(login_with_Id, "Login is null");
        String url = BASE_URL + "/read/" + login_with_Id.getLoginID();
        ResponseEntity<Login> response = restTemplate.getForEntity(url, Login.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(login_with_Id, "Login is null");
        String url = BASE_URL + "/update";
        Login updatedLogin = new Login.Builder()
                .copy(login_with_Id)
                .setPassword("SecurePass2025") // Example of updating the password
                .build();
        this.restTemplate.put(url, updatedLogin);

        // Verify if the object is updated using the read method
        ResponseEntity<Login> readLogin =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + updatedLogin.getLoginID(), Login.class);
        assertEquals(HttpStatus.OK, readLogin.getStatusCode());

        Login newLogin = readLogin.getBody();
        assertEquals("SecurePass2025", newLogin.getPassword());
        System.out.println("newLogin: " + newLogin);
    }

    @Test
    void d_getAllLogins() {
        String url = BASE_URL + "/getAllLogins";
        ResponseEntity<Login[]> response = restTemplate.getForEntity(url, Login[].class);
        assertNotNull(response.getBody());
        System.out.println("response: ");
        for (Login log : response.getBody()) {
            System.out.println(log);
        }
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/delete/" + login_with_Id.getLoginID();
        System.out.println("Deleted login: " + login_with_Id);
        this.restTemplate.delete(url);

        // Verify if the object is deleted
        ResponseEntity<Login> readLogin =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + login_with_Id.getLoginID(), Login.class);

        // Expect 404 after deletion
        assertEquals(HttpStatus.NOT_FOUND, readLogin.getStatusCode());
        System.out.println("After deletion, the readLogin status code: " + readLogin.getStatusCode());
    }
}