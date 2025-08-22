package za.ac.cput.repository.user.impl;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.User;
import za.ac.cput.factory.user.UserFactory;
import za.ac.cput.repository.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    private static UserRepository repository = new UserRepositoryImpl();
    private static User user1 = UserFactory.createUser("Kwanda", "Twalo");


    @Test
    @Order(1)
    void create() {
        User created = repository.create(user1);
        assertNotNull(created);
        System.out.println("created = " + created);
    }

    @Test
    @Order(2)
    void read() {
        User read = repository.read(user1.getUserId());
        assertNotNull(read);
        assertEquals(user1.getUserId(), read.getUserId());
        System.out.println("read login = " + read);
    }

    @Test
    @Order(3)
    void update() {
        User updatedUser = new User.Builder().copy(user1).setUserName("Thina").build();
        repository.update(updatedUser);
        assertNotNull(updatedUser);
        assertNotEquals(user1.getUserName(), updatedUser.getUserName());
        System.out.println("updated username = " + updatedUser);
    }

    @Test
    @Order(5)
    void delete() {
        repository.delete(user1.getUserId());
    }

    @Test
    @Order(4)
    void getUsers() {
        System.out.println("getUsers = " + repository.getUsers());
    }
}