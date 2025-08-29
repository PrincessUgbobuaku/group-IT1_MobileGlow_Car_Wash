package za.ac.cput.service.generic;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.repository.generic.IAddressRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private IAddressRepository repository;

    private static Address testAddress;
    private static Long savedAddressId;

    @Test
    void a_testCreateAddress() {
        Address address = new Address.Builder()
                .setStreetNumber("12A")
                .setStreetName("Main Street")
                .setCity("Cape Town")
                .setPostalCode("8000")
                .build();

        Address saved = addressService.create(address);
        assertNotNull(saved.getAddressID());
        assertEquals("Main Street", saved.getStreetName());
        assertEquals("Cape Town", saved.getCity());
        System.out.println("Created Address: " + saved);

        testAddress = saved;
        savedAddressId = saved.getAddressID();
    }

    @Test
    void b_testReadAddress() {
        assertNotNull(savedAddressId, "Address ID is null - create test might have failed");

        Address found = addressService.read(savedAddressId);
        assertNotNull(found);
        assertEquals(savedAddressId, found.getAddressID());
        assertEquals("Main Street", found.getStreetName());
        assertEquals("Cape Town", found.getCity());
        System.out.println("Read Address: " + found);
    }

    @Test
    void c_testUpdateAddress() {
        assertNotNull(testAddress, "Test address is null - create test might have failed");

        Address updatedAddress = new Address.Builder()
                .copy(testAddress)
                .setStreetNumber("45B")
                .setStreetName("Updated Street")
                .setCity("Johannesburg")
                .setPostalCode("2000")
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
    void d_testGetAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        assertNotNull(addresses);
        assertFalse(addresses.isEmpty());
        assertTrue(addresses.size() >= 1);

        System.out.println("All Addresses (" + addresses.size() + "):");
        for (Address address : addresses) {
            System.out.println(address);
            // Verify that our test address is in the list
            if (address.getAddressID().equals(savedAddressId)) {
                assertEquals("Updated Street", address.getStreetName());
                assertEquals("Johannesburg", address.getCity());
            }
        }
    }

    @Test
    void e_testDeleteAddress() {
        assertNotNull(savedAddressId, "Address ID is null - create test might have failed");

        // First verify the address exists
        Address beforeDelete = addressService.read(savedAddressId);
        assertNotNull(beforeDelete, "Address should exist before deletion");

        // Delete the address
        boolean deleteResult = addressService.delete(savedAddressId);
        assertTrue(deleteResult, "Delete operation should return true");

        // Verify the address no longer exists
        Address afterDelete = addressService.read(savedAddressId);
        assertNull(afterDelete, "Address should be null after deletion");

        System.out.println("Address deleted successfully. ID: " + savedAddressId);
    }


    }
