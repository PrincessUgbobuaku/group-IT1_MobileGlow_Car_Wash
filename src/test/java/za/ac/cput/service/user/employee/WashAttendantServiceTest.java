package za.ac.cput.service.user.employee;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.WashAttendantFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class WashAttendantServiceTest {

    @Autowired
    private WashAttendantService washAttendantService;

    private final Contact contact = ContactFactory.createContact("0725637252");

    private final Address address = AddressFactory.createAddressFactory1(
            "101", "Main Street", "Cape Town", "8000");

    private final Login login = LoginFactory.createLogin("washattendant@gmail.com", "password123");

    private final WashAttendant washAttendant = new WashAttendant.Builder()
            .setUserName("Mike")
            .setUserSurname("Johnson")
            .setIsActive(true)
            .setRoleDescription(User.RoleDescription.EMPLOYEE)
            .setEmployeeType("Wash Attendant")
            .setIsFullTime(true)
            .setShiftHours(8)
            .setContact(contact)
            .setAddress(address)
            .setLogin(login)
            .build();

    private WashAttendant washAttendantWithId;

    @Test
    @Rollback(value = false)
    void a_create() {
        WashAttendant created = washAttendantService.create(washAttendant);
        assertNotNull(created);
        washAttendantWithId = created;
        System.out.println("Created WashAttendant: " + created);
    }

    @Test
    @Rollback(value = false)
    void b_read() {
        assertNotNull(washAttendantWithId);
        WashAttendant read = washAttendantService.read(washAttendantWithId.getUserId());
        assertNotNull(read);
        assertEquals(washAttendantWithId.getUserId(), read.getUserId());
        System.out.println("Read WashAttendant: " + read);
    }

    @Test
    @Rollback(value = false)
    void c_update() {
        WashAttendant updated = new WashAttendant.Builder()
                .copy(washAttendantWithId)
                .setUserName("Michael")
                .setIsFullTime(false)
                .setShiftHours(6)
                .build();

        washAttendantService.update(updated);
        WashAttendant read = washAttendantService.read(washAttendantWithId.getUserId());

        assertEquals("Michael", read.getUserName());
        assertFalse(read.getIsFullTime());
        assertEquals(6, read.getShiftHours());
        System.out.println("Updated WashAttendant: " + read);
    }

    @Test
    void d_getAllWashAttendants() {
        List<WashAttendant> all = washAttendantService.getAllWashAttendants();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        System.out.println("All WashAttendants: " + all);
    }

//    @Test
//    void e_delete() {
//        boolean deleted = washAttendantService.delete(washAttendantWithId.getUserId());
//        assertTrue(deleted);
//        assertNull(washAttendantService.read(washAttendantWithId.getUserId()));
//        System.out.println("Deleted WashAttendant with ID: " + washAttendantWithId.getUserId());
//    }
}