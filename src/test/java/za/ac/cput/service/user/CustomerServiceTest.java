// Thaakirah Watson, 230037550
package za.ac.cput.service.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // Each test runs in its own transaction and rolls back
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        // Create Contact
        Contact contact1 = new Contact.Builder()
                .setContactID("C01")
                .setPhoneNumber("123-456-7890")
                .build();

        Contact contact2 = new Contact.Builder()
                .setContactID("C02")
                .setPhoneNumber("098-765-4321")
                .build();

        // Create Address
        Address address1 = new Address.Builder()
                .setAddressID("A01")
                .setStreetNumber("123")
                .setStreetName("Main Street")
                .setCity("Cape Town")
                .setPostalCode("8001")
                .build();

        Address address2 = new Address.Builder()
                .setAddressID("A02")
                .setStreetNumber("126")
                .setStreetName("Main Street")
                .setCity("Cape Town")
                .setPostalCode("8002")
                .build();

        // Create Login
        Login login1 = new Login.Builder()
                .setEmailAddress("johndoe@gmail.com")
                .setPassword("password123")
                .build();

        Login login2 = new Login.Builder()
                .setEmailAddress("janesmith@gmail.com")
                .setPassword("password456")
                .build();

        customer1 = new Customer.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .setIsActive(true)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setCustomerDOB(LocalDate.of(1990, 1, 1))
                .setContact(contact1)        // Add this
                .setAddress(address1)        // Add this
                .setLogin(login1)            // Add this
                .build();

        customer2 = new Customer.Builder()
                .setUserName("Jane")
                .setUserSurname("Smith")
                .setIsActive(false)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setCustomerDOB(LocalDate.of(1992, 5, 10))
                .setContact(contact2)        // Add this
                .setAddress(address2)        // Add this
                .setLogin(login2)            // Add this
                .build();

        customer1 = service.create(customer1);
        customer2 = service.create(customer2);
    }

    @Test
    @Rollback(false) //added by Princess
    void create() {
        System.out.println("=== Starting CREATE test ===");
        assertNotNull(service, "CustomerService should not be null");

        // Create Contact for the new customer
        System.out.println("Creating Contact...");
        Contact newContact = new Contact.Builder()
                .setContactID("C05")
                .setPhoneNumber("555-123-4567")
                .build();
        System.out.println("Contact created: " + newContact);

        // Create Address for the new customer
        System.out.println("Creating Address...");
        Address newAddress = new Address.Builder()
                .setAddressID("A05")
                .setStreetNumber("136")
                .setStreetName("Main Street")
                .setCity("Cape Town")
                .setPostalCode("8003")
                .build();
        System.out.println("Address created: " + newAddress);

        // Create Login for the new customer
        System.out.println("Creating Login...");
        Login newLogin = new Login.Builder()
                .setEmailAddress("test@gmail.com")
                .setPassword("password456")
                .build();
        System.out.println("Login created: " + newLogin);

        // Create the new Customer with all required relationships
        System.out.println("Creating Customer with all relationships...");
        Customer newCustomer = new Customer.Builder()
                .setUserName("Alice")
                .setUserSurname("Johnson")
                .setIsActive(true)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setCustomerDOB(LocalDate.of(1985, 12, 12))
                .setContact(newContact)     // Required relationship
                .setAddress(newAddress)     // Required relationship
                .setLogin(newLogin)         // Required relationship
                .build();
        System.out.println("Customer built (before save): " + newCustomer);

        System.out.println("Calling service.create()...");
        Customer created = service.create(newCustomer);
        System.out.println("CREATE Result: " + created);
        System.out.println("Created Customer ID: " + (created != null ? created.getUserId() : "null"));

        // Assertions
        System.out.println("Running assertions...");
        assertNotNull(created, "Created customer should not be null");
        assertNotNull(created.getUserId(), "Customer ID should be generated");
        assertEquals("Alice", created.getUserName());
        assertEquals("Johnson", created.getUserSurname());
        assertTrue(created.getIsActive());
        assertEquals(User.RoleDescription.CLIENT, created.getRoleDescription());

        // Assert that relationships were properly saved
        System.out.println("Checking relationships...");
        assertNotNull(created.getContact(), "Contact should not be null");
        assertNotNull(created.getAddress(), "Address should not be null");
        assertNotNull(created.getLogin(), "Login should not be null");

        System.out.println("Contact details: " + created.getContact());
        System.out.println("Address details: " + created.getAddress());
        System.out.println("Login details: " + created.getLogin());

        // Assert relationship details
        assertEquals("555-123-4567", created.getContact().getPhoneNumber());
        assertEquals("136", created.getAddress().getStreetNumber());
        assertEquals("test@gmail.com", created.getLogin().getEmailAddress());

        System.out.println("=== CREATE test completed successfully ===");
    }

    // Remove @Order annotations from other tests
    @Test
    @Rollback //added by Princess
    void read() {
        Customer readCustomer = service.read(customer1.getUserId());
        System.out.println("READ Result: " + readCustomer);
        assertNotNull(readCustomer);
        assertEquals(customer1.getUserName(), readCustomer.getUserName());
    }

    @Test
    void update() {
        Customer updatedCustomer = new Customer.Builder()
                .copy(customer1)
                .setUserSurname("UpdatedDoe")
                .build();

        Customer result = service.update(updatedCustomer);
        System.out.println("UPDATE Result: " + result);
        assertEquals("UpdatedDoe", result.getUserSurname());
    }

    @Test
    void delete() {
        boolean deleted = service.delete(customer2.getUserId());
        System.out.println("DELETE Result: " + deleted);
        assertTrue(deleted);
    }

    @Test
    void findBySurname() {
        List<Customer> results = service.findBySurname("Doe");
        System.out.println("FIND BY SURNAME Result: " + results);
        assertTrue(results.stream().anyMatch(c -> c.getUserSurname().equals("Doe")));
    }

    @Test
    void findActiveCustomers() {
        List<Customer> results = service.findActiveCustomers();
        System.out.println("FIND ACTIVE CUSTOMERS Result: " + results);
        assertTrue(results.stream().allMatch(Customer::getIsActive));
    }

    @Test
    void findAll() {
        List<Customer> allCustomers = service.findAll();
        System.out.println("FIND ALL Result: " + allCustomers);
        assertTrue(allCustomers.size() >= 2);
    }
}
