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
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.ManagerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class ManagerServiceImplTest {

    @Autowired
    private ManagerServiceImpl managerService;

    private static Contact contact = ContactFactory.createContactFactory1("0725637252");


    private static Address address = AddressFactory.createAddressFactory1("101",
            "Main Street",
            "Cape Town",
            "8000");

    private static Login login = LoginFactory.createLogin("kwndtwalo@gmail.com", "password34");

    private static Manager manager = ManagerFactory.createManager(
            "Sandile",
            "Sibiya",
            User.RoleDescription.EMPLOYEE,
            true,
            LocalDate.of(2025, 7, 1),
            "Manager",
            contact,
            address,
            login);

    private static Manager managerWithId;

    @Test
    void a_create() {
        Manager created =  managerService.create(manager);
        assertNotNull(created);
        managerWithId = created;
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {
        assertNotNull(managerWithId);
        Manager readManager = managerService.read(managerWithId.getUserId());
        assertNotNull(readManager);
        assertEquals(managerWithId.getUserId(), readManager.getUserId());
        System.out.println("Read successfully: " + readManager);
    }

    @Test
    void c_update() {
        Manager updatedManager = new Manager.Builder()
                .copy(managerWithId)
                .setUserName("Enough")
                .build();
        managerService.update(updatedManager);
        Manager readManager = managerService.read(managerWithId.getUserId());
        assertEquals(updatedManager.getUserName(), readManager.getUserName());
        System.out.println("Updated: " + updatedManager);
    }

    @Test
    void d_getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        assertNotNull(managers);
        System.out.println("Managers: " + managers);
    }

    @Test
    void e_delete() {
        managerService.delete(managerWithId.getUserId());
        assertNull(managerService.read(managerWithId.getUserId()));
        System.out.println("Delete successfully: " + managerWithId);
    }
}

