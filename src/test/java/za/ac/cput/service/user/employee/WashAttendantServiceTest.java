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
class WashAttendantServiceTest {

    @Autowired
    private WashAttendantService washAttendantService;

    private static Contact contact = ContactFactory.createContact("0625341234");

    private static Address address = AddressFactory.createAddressFactory1(
            "100", "School Street", "Cape Town", "8001");

    private static Login login = LoginFactory.createLogin("washattendant@gmail.com", "password123");

    private static WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
            "Matthew",
            "Lee",
            User.RoleDescription.EMPLOYEE,
            true,
            "WashAttendant",
            true,
            7,
            contact,
            address,
            login
            );

    private static WashAttendant washAttendantWithId;

    @Test
    void a_create() {
        WashAttendant created = washAttendantService.create(washAttendant);
        assertNotNull(created);
        washAttendantWithId = created;
        System.out.println("Created WashAttendant: " + washAttendantWithId);
    }

    @Test
    void b_read() {
        assertNotNull(washAttendantWithId);
        WashAttendant read = washAttendantService.read(washAttendantWithId.getUserId());
        assertNotNull(read);
        assertEquals(washAttendantWithId.getUserId(), read.getUserId());
        System.out.println("Read WashAttendant: " + read);
    }

    @Test
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

    @Test
    void e_delete() {
        washAttendantService.delete(washAttendantWithId.getUserId());
        assertNull(washAttendantService.read(washAttendantWithId.getUserId()));
        System.out.println("Deleted WashAttendant with ID: " + washAttendantWithId);
    }
}