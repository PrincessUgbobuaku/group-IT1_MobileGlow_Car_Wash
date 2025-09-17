/*
package za.ac.cput.service.user.employee.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.WashAttendantFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class WashAttendantServiceImplTest {

    @Autowired
    private WashAttendantServiceImpl washAttendantService;

    private static Contact contact = ContactFactory.createContactFactory1("0725637252");

    private static Address address = AddressFactory.createAddressFactory1("101",
            "Main Street",
            "Cape Town",
            "8000");

    private static Login login = LoginFactory.createLogin("washattendant@gmail.com", "password123");

    private static WashAttendant washAttendant = WashAttendantFactory.createWashAttendant(
            "Mike",
            "Johnson",
            User.RoleDescription.WASH_ATTENDANT,
            true,
            "Full-Time",
            true, // isFullTime
            8,    // shiftHours
            contact,
            address,
            login);

    private static WashAttendant washAttendantWithId;

    @Test
    void a_create() {
        WashAttendant created = washAttendantService.create(washAttendant);
        assertNotNull(created);
        washAttendantWithId = created;
        System.out.println("Created WashAttendant: " + created);
    }

    @Test
    void b_read() {
        assertNotNull(washAttendantWithId);
        WashAttendant readWashAttendant = washAttendantService.read(washAttendantWithId.getUserId());
        assertNotNull(readWashAttendant);
        assertEquals(washAttendantWithId.getUserId(), readWashAttendant.getUserId());
        System.out.println("Read successfully: " + readWashAttendant);
    }

    @Test
    void c_update() {
        WashAttendant updatedWashAttendant = new WashAttendant.Builder()
                .copy(washAttendantWithId)
                .setUserName("Michael")
                .setIsFullTime(false)
                .setShiftHours(6)
                .build();
        washAttendantService.update(updatedWashAttendant);
        WashAttendant readWashAttendant = washAttendantService.read(washAttendantWithId.getUserId());
        assertEquals(updatedWashAttendant.getUserName(), readWashAttendant.getUserName());
        assertEquals(updatedWashAttendant.getIsFullTime(), readWashAttendant.getIsFullTime());
        assertEquals(updatedWashAttendant.getShiftHours(), readWashAttendant.getShiftHours());
        System.out.println("Updated: " + updatedWashAttendant);
    }

    @Test
    void d_getAllWashAttendants() {
        java.util.List<WashAttendant> washAttendants = washAttendantService.getAllWashAttendants();
        assertNotNull(washAttendants);
        assertFalse(washAttendants.isEmpty());
        System.out.println("All WashAttendants: " + washAttendants);
    }

    @Test
    void e_delete() {
        washAttendantService.delete(washAttendantWithId.getUserId());
        assertNull(washAttendantService.read(washAttendantWithId.getUserId()));
        System.out.println("Delete successfully: " + washAttendantWithId.getUserId());
    }
}*/
