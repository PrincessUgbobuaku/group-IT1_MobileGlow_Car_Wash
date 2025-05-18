package za.ac.cput.factory.generic;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.generic.Address;

import static org.junit.jupiter.api.Assertions.*;

class AddressFactoryTest {

    @Test
    void testBuildValidAddress() {
        Address address = AddressFactory.createAddressFactory1("123", "Main Street", "Cape Town", "8001");

        // Test that address was created
        assertNotNull(address);
        assertEquals("123", address.getStreetNumber());
        assertEquals("Main Street", address.getStreetName());
        assertEquals("Cape Town", address.getCity());
        assertEquals("8001", address.getPostalCode());

        System.out.println(address);
    }

    @Test
        // Should return null when streetNumber is null
    void testBuildInvalidAddressReturnsNull() {
        Address address = AddressFactory.createAddressFactory1(null, "Main Street", "Cape Town", "8001");
        assertNull(address);
    }
}

