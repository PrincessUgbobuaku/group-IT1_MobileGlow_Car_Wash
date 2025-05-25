/*
 Student name : Thaakirah Watson
 Student number: 230037550
 Description   : Test class for CustomerFactory
*/

package za.ac.cput.factory.user;

import org.junit.Test;
import za.ac.cput.domain.user.Customer;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CustomerFactoryTest {

    @Test
    public void testBuildCustomerSuccess() {
        Customer customer = CustomerFactory.build1(LocalDate.of(1995, 6, 15));

        assertNotNull(customer);
        assertNotNull(customer.getCustomerID());
        assertFalse(customer.getCustomerID().isEmpty());
        assertEquals(LocalDate.of(1995, 6, 15), customer.getCustomerDOB());
    }

    @Test
    public void testBuildCustomerWithInvalidDate() {
        try {
            CustomerFactory.build1(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("CustomerDOB must not be null"));
        }
    }
}
