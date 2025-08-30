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
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.factory.generic.ContactFactory;
import za.ac.cput.factory.user.LoginFactory;
import za.ac.cput.factory.user.employee.AccountantFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class AccountantServiceTest {

    @Autowired
    private AccountantService accountantService;

    private static Contact contact = ContactFactory.createContactFactory1("0725637252");

    private static Address address = AddressFactory.createAddressFactory1("101",
            "Main Street",
            "Cape Town",
            "8000");

    private static Login login = LoginFactory.createLogin("accountant@gmail.com", "password123");

    private static Accountant accountant = AccountantFactory.createAccountant(
            "John",
            "Doe",
            User.RoleDescription.ACCOUNTANT,
            true,
            "Full-Time",
            true, // hasTaxFillingAuthority
            contact,
            address,
            login);

    private static Accountant accountantWithId;

    @Test
    void a_create() {
        Accountant created = accountantService.create(accountant);
        assertNotNull(created);
        accountantWithId = created;
        System.out.println("Created Accountant: " + created);
    }

    @Test
    void b_read() {
        assertNotNull(accountantWithId);
        Accountant readAccountant = accountantService.read(accountantWithId.getUserId());
        assertNotNull(readAccountant);
        assertEquals(accountantWithId.getUserId(), readAccountant.getUserId());
        System.out.println("Read successfully: " + readAccountant);
    }

    @Test
    void c_update() {
        Accountant updatedAccountant = new Accountant.Builder()
                .copy(accountantWithId)
                .setUserName("Jonathan")
                .setHasTaxFillingAuthority(false)
                .build();
        accountantService.update(updatedAccountant);
        Accountant readAccountant = accountantService.read(accountantWithId.getUserId());
        assertEquals(updatedAccountant.getUserName(), readAccountant.getUserName());
        assertEquals(updatedAccountant.getHasTaxFillingAuthority(), readAccountant.getHasTaxFillingAuthority());
        System.out.println("Updated: " + updatedAccountant);
    }

    @Test
    void d_getAllAccountants() {
        java.util.List<Accountant> accountants = accountantService.getAllAccountants();
        assertNotNull(accountants);
        assertFalse(accountants.isEmpty());
        System.out.println("All Accountants: " + accountants);
    }

    @Test
    void e_delete() {
        accountantService.delete(accountantWithId.getUserId());
        assertNull(accountantService.read(accountantWithId.getUserId()));
        System.out.println("Delete successfully: " + accountantWithId.getUserId());
    }
}
