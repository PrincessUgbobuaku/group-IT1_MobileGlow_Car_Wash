package za.ac.cput.repository.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.repository.generic.impl.IAddressRepository;

import static org.junit.jupiter.api.Assertions.*;

public class AddressRepositoryTest {

    private AddressRepository repository;
    private Address address;

    @BeforeEach
    void setUp() {
        repository = new AddressRepository();
        address = AddressFactory.createAddressFactory1("123", "Main Street", "Cape Town", "8001");
        repository.create(address);
    }

    @Test
    void testRead() {
        assertNotNull(repository.read(address.getAddressID()));
    }

    @Test
    void testUpdate() {
        Address updated = new Address.Builder()
                .copy(address)
                .setCity("Johannesburg")
                .build();
        assertNotNull(repository.update(updated));
        assertEquals("Johannesburg", repository.read(address.getAddressID()).getCity());
    }

    @Test
    void testDelete() {
        assertTrue(repository.delete(address.getAddressID()));
        assertNull(repository.read(address.getAddressID()));
    }
}
