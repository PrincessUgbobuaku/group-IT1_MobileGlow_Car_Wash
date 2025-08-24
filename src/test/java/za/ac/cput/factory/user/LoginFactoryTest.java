package za.ac.cput.factory.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.Login;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginFactoryTest {
    private Login l1 = LoginFactory.createLogin("218120192@mycput.ac.za", "123456");
    private Login l2 = LoginFactory.createLogin("218120192mycput.ac.za", "123456");
    private Login l3 = LoginFactory.createLogin("218120192@mycput.ac.za", "");

    @Test
    @Order(1)
    public void testCreateLoginObj() {
        //Test should pass if all values are filled.
        assertNotNull(l1);
        System.out.println(l1.toString());//For this to be printed it needs a toString in Login Domain.
    }

    @Test
    @Order(2)
    public void testInValidEmail() {
        //this test should fail because there's no @ in the email.
        assertNotNull(l2);
        System.out.println(l2.toString());
    }

    @Test
    @Order(3)
    public void testAllValuesAreFilled() {
        //this test should fail because there are missing values.
        assertNotNull(l3);
        System.out.println(l3.toString());
    }




}
