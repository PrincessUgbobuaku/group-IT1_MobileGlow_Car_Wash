package za.ac.cput.repository.user.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.Login;
import za.ac.cput.factory.user.LoginFactory;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRepositoryImplTest {

    private static LoginRepositoryImpl repository = new LoginRepositoryImpl();
    private static Login login1 = LoginFactory.createLogin("twalo@gmail.com", "123456");

    @Test
    @Order(1)
    void create() {
        Login createLogin = repository.create(login1);
        assertNotNull(createLogin);
        System.out.println("Login created: " + createLogin);
    }

    @Test
    @Order(2)
    void read() {
        Login readLogin = repository.read(login1.getLoginID());
        assertNotNull(readLogin);
        assertEquals(login1.getLoginID(), readLogin.getLoginID());
        System.out.println("Login read: " + readLogin);
    }

    @Test
    @Order(3)
    void update() {
        Login updatedLogin = new Login.Builder().copy(login1).setPassword("654321").build();
        updatedLogin = repository.update(updatedLogin);
        assertNotNull(updatedLogin);
        assertNotEquals(updatedLogin.getPassword(), login1.getPassword());
        System.out.println("Login updated: " + updatedLogin);
    }

    @Test
    @Order(5)
    void delete() {
        repository.delete(login1.getLoginID());
    }

    @Test
    void getLogins() {
        System.out.println("List of logins: " + repository.getLogins());
    }


}