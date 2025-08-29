// Thaakirah Watson, 230037550
package za.ac.cput.factory.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleFactoryTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        // Fully built Login, Address, Contact objects
        Login login = new Login.Builder()
                .setLoginID(1L)
                .setEmailAddress("john.doe@example.com")
                .setPassword("password123")
                .build();

        Address address = new Address.Builder()
                .setAddressID("001")
                .setStreetNumber("123")
                .setStreetName("Main str.")
                .setCity("Cape Town")
                .setPostalCode("12345")
                .build();

        Contact contact = new Contact.Builder()
                .setContactID("001")
                .setPhoneNumber("+27123456789")
                .build();

        // Fully built Customer with all attributes
        customer = new Customer.Builder()
                .setUserId(1L)
                .setUserName("John")
                .setUserSurname("Doe")
                .setIsActive(true)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setLogin(login)
                .setAddress(address)
                .setContact(contact)
                .setCustomerDOB(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void createVehicle() {
        Vehicle vehicle = VehicleFactory.createVehicle(
                "ABC123",
                "Toyota",
                "Red",
                "Corolla",
                customer
        );

        System.out.println("CREATE VEHICLE Result: " + vehicle);
        assertNotNull(vehicle);
        assertEquals("ABC123", vehicle.getCarPlateNumber());
        assertEquals(customer, vehicle.getCustomer());

        // Invalid plate number
        Exception ex1 = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.createVehicle(
                        "",
                        "Toyota",
                        "Red",
                        "Corolla",
                        customer
                ));
        System.out.println("EXPECTED Exception (plate): " + ex1.getMessage());

        // Invalid make
        Exception ex2 = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.createVehicle(
                        "DEF456",
                        "",
                        "Blue",
                        "Civic",
                        customer
                ));
        System.out.println("EXPECTED Exception (make): " + ex2.getMessage());

        // Invalid model
        Exception ex3 = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.createVehicle(
                        "GHI789",
                        "Honda",
                        "Green",
                        "",
                        customer
                ));
        System.out.println("EXPECTED Exception (model): " + ex3.getMessage());

        // Null customer
        Exception ex4 = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.createVehicle(
                        "JKL012",
                        "Ford",
                        "Black",
                        "Focus",
                        null
                ));
        System.out.println("EXPECTED Exception (customer): " + ex4.getMessage());
    }
}
