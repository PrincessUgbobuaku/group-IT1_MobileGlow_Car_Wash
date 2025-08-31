//Thaakirah Watson, 230037550
package za.ac.cput.factory.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactoryTest {

    private Login login;
    private Address address;
    private Contact contact;

    @BeforeEach
    void setUp() {
        // Example login, address, contact objects
        login = new Login.Builder()
                .setLoginID(1L)
                .setEmailAddress("john.doe@example.com")
                .setPassword("password123")
                .build();

        address = new Address.Builder()
                .setAddressID(111L)
                .setStreetNumber("123")
                .setStreetName("Main str.")
                .setCity("Cape Town")
                .setPostalCode("12345")
                .build();

        contact = new Contact.Builder()
                .setContactID(222L)
                .setPhoneNumber("+27123456789")
                .build();
    }

    @Test
    void createCustomer() {
        // Happy path with userId
        Customer customer = CustomerFactory.createCustomer(
                1L,
                "John",
                "Doe",
                true,
                Customer.RoleDescription.CLIENT,
                login,
                address,
                contact,
                LocalDate.of(1990, 1, 1)
        );

        System.out.println("CREATE CUSTOMER Result: " + customer);
        assertNotNull(customer);
        assertEquals("John", customer.getUserName());
        assertEquals("Doe", customer.getUserSurname());
        assertEquals(1L, customer.getUserId());

        // Invalid name
        Exception ex1 = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.createCustomer(
                        2L,
                        "",
                        "Doe",
                        true,
                        Customer.RoleDescription.CLIENT,
                        login,
                        address,
                        contact,
                        LocalDate.of(1990, 1, 1)
                ));
        System.out.println("EXPECTED Exception (name): " + ex1.getMessage());

        // Invalid surname
        Exception ex2 = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.createCustomer(
                        3L,
                        "John",
                        "",
                        true,
                        Customer.RoleDescription.CLIENT,
                        login,
                        address,
                        contact,
                        LocalDate.of(1990, 1, 1)
                ));
        System.out.println("EXPECTED Exception (surname): " + ex2.getMessage());

        // Null DOB
        Exception ex3 = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.createCustomer(
                        4L,
                        "John",
                        "Doe",
                        true,
                        Customer.RoleDescription.CLIENT,
                        login,
                        address,
                        contact,
                        null
                ));
        System.out.println("EXPECTED Exception (DOB): " + ex3.getMessage());
    }
}