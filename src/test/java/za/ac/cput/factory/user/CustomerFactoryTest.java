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
        Customer customer = CustomerFactory.build1(LocalDate.of(1995, 6, 15));

        assertNotNull(customer);
        assertNotNull(customer.getCustomerID());
        assertFalse(customer.getCustomerID().isBlank());
        assertEquals(LocalDate.of(1995, 6, 15), customer.getCustomerDOB());
    }

    @Test
    void testBuildCustomerWithInvalidDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CustomerFactory.build1(null)
        );
        assertTrue(exception.getMessage().contains("CustomerDOB must not be null"));
    }
}
