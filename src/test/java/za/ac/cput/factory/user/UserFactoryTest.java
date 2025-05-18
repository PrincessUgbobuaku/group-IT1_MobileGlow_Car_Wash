package za.ac.cput.factory.user;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserFactoryTest {

    private User user1 = UserFactory.createUser("John", "Doe");
    private User user2 = UserFactory.createUser("", "Doe");

    @Test
    @Order(1)
    public void testCreateUserWithAllFields() {
        //This test should pass There are no missing values.
        assertNotNull(user1);
        System.out.println(user1.toString());
    }

    @Test
    @Order(2)
    public void TestUserWithMissingFields() {
        //This test should fail because there are missing values.
        assertNotNull(user2);
        System.out.println(user2.toString());
    }
}
