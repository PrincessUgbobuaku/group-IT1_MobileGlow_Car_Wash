package za.ac.cput.factory.user.employee;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.factory.user.LoginFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccountantFactoryTest {

    Contact contact = new Contact.Builder()
            .setPhoneNumber("0725637252")
            .build();

    Address address = new Address.Builder()
            .setStreetNumber("101")
            .setStreetName("Main Street")
            .setCity("Cape Town")
            .setPostalCode("8000")
            .build();

    private Login login = LoginFactory.createLogin("accountant@mycput.ac.za", "accountant123");

    // Act
    public Accountant accountant = AccountantFactory.createAccountant(
            "Abulele",
            "Voki",
            User.RoleDescription.ACCOUNTANT,
            true,
            "Senior Accountant",
            true,
            contact,
            address,
            login);

    @Test
    public void createAccountant() {
        assertNotNull(accountant);
        System.out.println("Accountant was created successfully: " + accountant);
    }

    @Test
    public void testInvalidTaxAuthority() {
        Accountant accountant = AccountantFactory.createAccountant(
                "Abulele",
                "Voki",
                User.RoleDescription.ACCOUNTANT,
                true,
                "Senior Accountant",
                false, // Invalid tax authority (assuming false is invalid based on validation)
                contact,
                address,
                login);

        assertNull(accountant);
        System.out.println("Accountant creation failed due to invalid tax authority.");
    }

    @Test
    public void testEmptyFields() {
        Accountant accountant = AccountantFactory.createAccountant(
                "", // Empty first name
                "Voki",
                User.RoleDescription.ACCOUNTANT,
                true,
                "Senior Accountant",
                true,
                contact,
                address,
                login);

        assertNull(accountant);
        System.out.println("Accountant creation failed due to empty field(s).");
    }

    @Test
    public void testNullEmployeeType() {
        Accountant accountant = AccountantFactory.createAccountant(
                "Abulele",
                "Voki",
                User.RoleDescription.ACCOUNTANT,
                true,
                null, // Null employee type
                true,
                contact,
                address,
                login);

        assertNull(accountant);
        System.out.println("Accountant creation failed due to null employee type.");
    }

    @Test
    public void testValidAccountantWithAllAttributes() {
        Accountant accountant = AccountantFactory.createAccountant(
                "Thandi",
                "Mbeki",
                User.RoleDescription.ACCOUNTANT,
                true,
                "Junior Accountant",
                true,
                contact,
                address,
                login);

        assertNotNull(accountant);
        System.out.println("Accountant with all valid attributes created successfully: " + accountant);
    }
}
