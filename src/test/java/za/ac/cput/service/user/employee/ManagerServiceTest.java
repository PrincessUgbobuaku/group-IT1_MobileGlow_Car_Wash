package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.user.Login;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.service.user.LoginService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ManagerServiceTest {

    private static Login login = LoginFactory.createLogin(
            "kwanda@yahoo.com",
            "Broker2000"
    );

    @Autowired
    private LoginService loginService;


    @Test
    void a_create() {
        Login createdLogin = loginService.create(login);
        assertNotNull(createdLogin);
        System.out.println("created login: " + createdLogin);
    }

    @Test
    void b_read() {
        Login readLogin = loginService.read(login.getLoginID());
        assertNotNull(readLogin);
        assertEquals(login.getLoginID(), readLogin.getLoginID());
        System.out.println("read login: " + readLogin);
    }

    @Test
    void c_update() {
        Login updatedLogin = new Login.Builder()
                .copy(login)
                .setEmailAddress("Twalo@yahoo.com")
                .build();
        loginService.update(updatedLogin);

        Login readLogin = loginService.read(updatedLogin.getLoginID());

        assertEquals(updatedLogin.getEmailAddress(), readLogin.getEmailAddress());
        assertEquals("Twalo@yahoo.com", updatedLogin.getEmailAddress());
        System.out.println("update login: " + updatedLogin);
    }

    @Test
    void d_getAllLogins() {
        List<Login> logins = loginService.getAllLogins();
        assertNotNull(logins);
        System.out.println("getAllLogins: " + logins);
    }

    @Test
    void e_delete() {
        loginService.delete(login.getLoginID());
        assertNull(loginService.read(login.getLoginID()));
        System.out.println("Deleted login: " + login);
    }

}
