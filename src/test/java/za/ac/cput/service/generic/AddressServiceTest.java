package za.ac.cput.service.generic;

/* MobileGlow Car Wash
   Address Service Test class
   Author: Inga Zekani (221043756)
 */

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.factory.generic.AddressFactory;
import za.ac.cput.repository.generic.IAddressRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private IAddressRepository repository;

    private static Address testAddress;
    private static Address savedAddressId;

    @Test
    void a_testCreateAddress() {
        // Use Factory class to create address
        testAddress = AddressFactory.createAddressFactory1(
                "12A",
                "Main Street",
                "Cape Town",
                "8000");
        assertNotNull(testAddress, "Factory should create a valid address");
        assertNull(testAddress.getAddressID(), "New address should not have ID before saving");
        assertEquals("Main Street", testAddress.getStreetName());
        assertEquals("Cape Town", testAddress.getCity());

        Address saved = addressService.create(testAddress);
        assertNotNull(saved);
        assertNotNull(saved.getAddressID(), "Saved address should have an ID");
        assertEquals("Main Street", saved.getStreetName());
        assertEquals("Cape Town", saved.getCity());

        savedAddressId = saved;
        System.out.println("Created Address: " + saved);

    }


    @Test
    void c_testReadAddress() {
        assertNotNull(savedAddressId, "Address ID is null - create test might have failed");

        Address found = addressService.read(savedAddressId.getAddressID());
        assertNotNull(found);
        //assertEquals(savedAddressId, found.getAddressID());
        assertEquals("Main Street", found.getStreetName());
        assertEquals("Cape Town", found.getCity());
        System.out.println("Read Address: " + found);
    }

    @Test
    void d_testUpdateAddress() {
        assertNotNull(testAddress, "Test address is null - create test might have failed");

        // Create updated address using factory with new values
        Address updatedAddress = AddressFactory.createAddressFactory1("45B", "Updated Street", "Johannesburg", "2000");
        assertNotNull(updatedAddress, "Factory should create updated address");

        // Set the same ID as the original address for update
        updatedAddress = new Address.Builder()
                .copy(updatedAddress)
                .setAddressID(testAddress.getAddressID())
                .build();

        Address updated = addressService.update(updatedAddress);
        assertNotNull(updated);
        assertEquals(testAddress.getAddressID(), updated.getAddressID());
        assertEquals("45B", updated.getStreetNumber());
        assertEquals("Updated Street", updated.getStreetName());
        assertEquals("Johannesburg", updated.getCity());
        assertEquals("2000", updated.getPostalCode());
        System.out.println("Updated Address: " + updated);

        testAddress = updated; // Update the reference for later tests
    }

    @Test
    void e_testGetAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        assertTrue(addresses.size() >= 1);

        System.out.println("All Addresses (" + addresses.size() + "):");
        for (Address address : addresses) {
            System.out.println(address);
            // Verify that our test address is in the list
            if (address.getAddressID().equals(savedAddressId.getAddressID())) {
                assertEquals("Updated Street", address.getStreetName());
                assertEquals("Johannesburg", address.getCity());
            }

            // Verify all addresses have valid data (testing factory validation indirectly)
            assertNotNull(address.getStreetNumber());
            assertNotNull(address.getStreetName());
            assertNotNull(address.getCity());
            assertNotNull(address.getPostalCode());
        }
    }

    @Test
    void f_testDeleteAddress() {
        assertNotNull(savedAddressId, "Address ID is null - create test might have failed");

        // First verify the address exists
        Address beforeDelete = addressService.read(savedAddressId.getAddressID());
        assertNotNull(beforeDelete, "Address should exist before deletion");

        // Delete the address
        boolean deleteResult = addressService.delete(savedAddressId.getAddressID());
        assertTrue(deleteResult, "Delete operation should return true");
        

        System.out.println("Address deleted successfully. ID: " + savedAddressId);
    }

}
