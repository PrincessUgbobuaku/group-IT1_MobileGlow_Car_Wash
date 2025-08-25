package za.ac.cput.factory.user.employee;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.factory.user.LoginFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ManagerFactoryTest {




    Contact contact = new Contact.Builder()
            .setPhoneNumber("0725637252")
            .build();

    Address address = new Address.Builder()
            .setStreetNumber("101")
            .setStreetName("Main Street")
            .setCity("Cape Town")
            .setPostalCode("8000")
            .build();

    private Login login = LoginFactory.createLogin("218120192@mycput.ac.za", "student123");

    LocalDate hireDate = LocalDate.of(2023, 5, 10);

    // Act
    public Manager manager = ManagerFactory.createManager(
            "Abulele",
            "Voki",
            User.RoleDescription.EMPLOYEE,
            true,
            hireDate,
            "Manager",
            contact,
            address,
            login);

    @Test
    public void createManager() {
        assertNotNull(manager);
        System.out.println("Manager was created successfully: " + manager);
    }

    @Test
    public void testInvalidHireDate() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        Manager manager = ManagerFactory.createManager(
                "Abulele",
                "Voki",
                User.RoleDescription.EMPLOYEE,
                true,
                futureDate,
                "Manager",
                contact,
                address,
                login);

        assertNull(manager);
        System.out.println("Manager creation failed due to invalid hire date since we can't put future date.");
    }

    @Test
    public void testEmptyFields() {

        Manager manager = ManagerFactory.createManager(
                "",
                "Voki",
                User.RoleDescription.EMPLOYEE,
                true,
                hireDate,
                "Manager",
                contact,
                address,
                login);

        assertNull(manager);
        System.out.println("Manager creation failed due to empty field(s).");
    }


}
