package za.ac.cput.factory.user.employee;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.user.LoginFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WashAttendantFactoryTest {

    Contact contact = new Contact.Builder()
            .setPhoneNumber("0725637252")
            .build();

    Address address = new Address.Builder()
            .setStreetNumber("101")
            .setStreetName("Main Street")
            .setCity("Cape Town")
            .setPostalCode("8000")
            .build();

    private Login login = LoginFactory.createLogin("washattendant@mycput.ac.za", "wash123");

    // Act
    public WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
            "Abulele",
            "Voki",
            User.RoleDescription.WASH_ATTENDANT,
            true,
            "Car Wash Attendant",
            true,
            8,
            contact,
            address,
            login);

    @Test
    public void createWashAttendant() {
        assertNotNull(washAttendant);
        System.out.println("Wash Attendant was created successfully: " + washAttendant);
    }

    @Test
    public void testInvalidShiftHours() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "Abulele",
                "Voki",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Car Wash Attendant",
                true,
                15, // Invalid shift hours (assuming validation fails for 15 hours)
                contact,
                address,
                login);

        assertNull(washAttendant);
        System.out.println("Wash Attendant creation failed due to invalid shift hours.");
    }

    @Test
    public void testEmptyFields() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "", // Empty first name
                "Voki",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Car Wash Attendant",
                true,
                8,
                contact,
                address,
                login);

        assertNull(washAttendant);
        System.out.println("Wash Attendant creation failed due to empty field(s).");
    }

    @Test
    public void testNullContact() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "Abulele",
                "Voki",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Car Wash Attendant",
                true,
                8,
                null, // Null contact
                address,
                login);

        assertNull(washAttendant);
        System.out.println("Wash Attendant creation failed due to null contact.");
    }

    @Test
    public void testNullAddress() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "Abulele",
                "Voki",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Car Wash Attendant",
                true,
                8,
                contact,
                null, // Null address
                login);

        assertNull(washAttendant);
        System.out.println("Wash Attendant creation failed due to null address.");
    }

    @Test
    public void testNullLogin() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "Dumiza",
                "Zikalala",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Car Wash Attendant",
                true,
                8,
                contact,
                address,
                null); // Null login

        assertNull(washAttendant);
        System.out.println("Wash Attendant creation failed due to null login.");
    }

    @Test
    public void testValidWashAttendantWithAllAttributes() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
                "Thuso",
                "Siduka",
                User.RoleDescription.WASH_ATTENDANT,
                true,
                "Senior Wash Attendant",
                false, // Part-time
                6,
                contact,
                address,
                login);

        assertNotNull(washAttendant);
        System.out.println("Wash Attendant with all valid attributes created successfully: " + washAttendant);
    }
}
