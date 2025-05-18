/*
 Student name : Thaakirah Watson
 Student number: 230037550
 Description   : Test class for CustomerFactory
*/

package za.ac.cput.factory.user;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.Customer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactoryTest {

    @Test
    void testBuildCustomerSuccess() {
        Customer customer = CustomerFactory.build("CUST001", LocalDate.of(1995, 6, 15));

        assertNotNull(customer);
        assertEquals("CUST001", customer.getCustomerID());
        assertEquals(LocalDate.of(1995, 6, 15), customer.getCustomerDOB());
    }

    @Test
    void testBuildCustomerWithInvalidDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.build("CUST002", null)
        );
        assertTrue(exception.getMessage().contains("CustomerDOB must not be null"));
    }

    @Test
    void testBuildCustomerWithEmptyID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.build("", LocalDate.of(1990, 1, 1))
        );
        assertTrue(exception.getMessage().contains("CustomerID must not be null or empty"));
    }
}
