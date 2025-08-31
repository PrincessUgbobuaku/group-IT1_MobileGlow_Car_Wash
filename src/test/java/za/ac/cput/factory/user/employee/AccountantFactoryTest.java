//package za.ac.cput.factory.user.employee;
//
//import org.junit.jupiter.api.Test;
//import za.ac.cput.domain.user.employee.Accountant;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AccountantFactoryTest {
//
//    @Test
//    public void testCreateAccountant() {
//        // Test creating an accountant with valid authority (true)
//        Accountant accountant = AccountantFactory.createAccountant(true);
//        System.out.println("Valid Accountant created: " + accountant);
//
//        // Verify the accountant was created successfully
//        assertNotNull(accountant);
//        assertNotNull(accountant.getUserId());
//        assertTrue(accountant.getHasTaxFillingAuthority());
//
//    }
//
//    @Test
//    public void testCreateAccountantWithValidAuthority() {
//        // Test creating an accountant with valid authority (true)
//        Accountant accountant = AccountantFactory.createAccountant(true);
//        System.out.println("Valid Accountant created: " + accountant);
//
//        // Verify the accountant was created successfully
//        assertNotNull(accountant);
//        assertNotNull(accountant.getUserId());
//        assertTrue(accountant.getHasTaxFillingAuthority());
//
//    }
//
//    @Test
//    public void testCreateAccountantWithUniqueIdentifier() {
//        Accountant accountant1 = AccountantFactory.createAccountant(true);
//        Accountant accountant2 = AccountantFactory.createAccountant(true);
//        System.out.println("Valid Accountant created: " + accountant1);
//        System.out.println("Valid Accountant created: " + accountant2);
//
//        assertNotNull(accountant1);
//        assertNotNull(accountant2);
//        assertNotNull(accountant1.getUserId());
//        assertNotNull(accountant2.getUserId());
//
//        // Verify that the IDs are unique
//        assertNotEquals(accountant1.getUserId(), accountant2.getUserId());
//     }
//    }
//
