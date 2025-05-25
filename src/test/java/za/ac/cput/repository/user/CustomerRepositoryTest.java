/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository Test class for Customer
 */

package za.ac.cput.repository.user;

import org.junit.Before;
import org.junit.Test;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.repository.user.impl.ICustomerRepository;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CustomerRepositoryTest {

    private ICustomerRepository repository;
    private Customer customer;

    @Before
    public void setUp() {
        repository = ICustomerRepository.getRepository();
        customer = new Customer.Builder()
                .setCustomerID("C001")
                .setCustomerDOB(LocalDate.of(2000, 1, 15))
                .build();
        repository.create(customer);
    }

    @Test
    public void testCreate() {
        Customer created = repository.create(customer);
        assertNotNull(created);
        assertEquals("C001", created.getCustomerID());
    }

    @Test
    public void testRead() {
        Customer read = repository.read("C001");
        assertNotNull(read);
        assertEquals(LocalDate.of(2000, 1, 15), read.getCustomerDOB());
    }

    @Test
    public void testUpdate() {
        Customer updated = new Customer.Builder()
                .setCustomerID("C001")
                .setCustomerDOB(LocalDate.of(1999, 12, 1)) // new DOB
                .build();
        repository.update(updated);
        Customer result = repository.read("C001");
        assertEquals(LocalDate.of(1999, 12, 1), result.getCustomerDOB());
    }

    @Test
    public void testDelete() {
        boolean deleted = repository.delete("C001");
        assertTrue(deleted);
        assertNull(repository.read("C001"));
    }

    @Test
    public void testGetAll() {
        assertFalse(repository.getAll().isEmpty());
    }
}