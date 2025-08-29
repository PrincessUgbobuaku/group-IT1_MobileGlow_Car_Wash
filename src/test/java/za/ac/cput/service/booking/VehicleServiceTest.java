// Thaakirah Watson, 230037550
package za.ac.cput.service.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.User;
import za.ac.cput.service.user.CustomerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VehicleServiceTest {

    @Autowired
    private VehicleService service;

    @Autowired
    private CustomerService customerService;
    private Customer customer;
    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        customer = new Customer.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .setIsActive(true)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setCustomerDOB(LocalDate.of(1990, 1, 1))
                .build();

        // ✅ Save customer first
        customer = customerService.create(customer);

        vehicle1 = new Vehicle.Builder()
                .setCarPlateNumber("ABC123")
                .setCarMake("Toyota")
                .setCarColour("Red")
                .setCarModel("Corolla")
                .setCustomer(customer)
                .build();

        vehicle2 = new Vehicle.Builder()
                .setCarPlateNumber("XYZ789")
                .setCarMake("Honda")
                .setCarColour("Blue")
                .setCarModel("Civic")
                .setCustomer(customer)
                .build();

        vehicle1 = service.create(vehicle1);
        vehicle2 = service.create(vehicle2);
    }

    @Test
    @Order(1)
    void create() {
        Vehicle newVehicle = new Vehicle.Builder()
                .setCarPlateNumber("LMN456")
                .setCarMake("Ford")
                .setCarColour("Black")
                .setCarModel("Focus")
                .setCustomer(customer)
                .build();

        Vehicle created = service.create(newVehicle);
        System.out.println("CREATE Result: " + created);
        assertNotNull(created.getVehicleID());
        assertEquals("LMN456", created.getCarPlateNumber());
    }

    @Test
    @Order(2)
    void read() {
        Vehicle readVehicle = service.read(vehicle1.getVehicleID());
        System.out.println("READ Result: " + readVehicle);
        assertNotNull(readVehicle);
        assertEquals(vehicle1.getCarPlateNumber(), readVehicle.getCarPlateNumber());
    }

    @Test
    @Order(3)
    void findByPlateNumber() {
        // Test with the original plate number before any updates
        Optional<Vehicle> result = service.findByPlateNumber(vehicle1.getCarPlateNumber());
        System.out.println("FIND BY PLATE Result: " + result.orElse(null));
        assertTrue(result.isPresent(), "Vehicle with plate number should be found");
        assertEquals(vehicle1.getCarPlateNumber(), result.get().getCarPlateNumber());
    }

    @Test
    @Order(4)
    void update() {
        Vehicle updatedVehicle = new Vehicle.Builder()
                .copy(vehicle1)
                .setCarPlateNumber("UPDATED123")
                .build();

        Vehicle result = service.update(updatedVehicle);
        System.out.println("UPDATE Result: " + result);
        assertEquals("UPDATED123", result.getCarPlateNumber());

        // Update the reference for other tests
        vehicle1 = result;
    }

    @Test
    @Order(5)
    void findByCustomerId() {
        List<Vehicle> vehicles = service.findByCustomerId(customer.getUserId());
        System.out.println("FIND BY CUSTOMER ID Result: " + vehicles);
        assertFalse(vehicles.isEmpty());
        assertTrue(vehicles.size() >= 2, "Should find at least 2 vehicles for this customer");
    }

    @Test
    @Order(6)
    void findAll() {
        List<Vehicle> allVehicles = service.findAll();
        System.out.println("FIND ALL Result: " + allVehicles);
        assertTrue(allVehicles.size() >= 2, "Should find at least 2 vehicles in total");
    }

    @Test
    @Order(7)
    void delete() {
        boolean deleted = service.delete(vehicle2.getVehicleID());
        System.out.println("DELETE Result: " + deleted);
        assertTrue(deleted, "Vehicle should be successfully deleted");

        // Verify deletion
        Vehicle deletedVehicle = service.read(vehicle2.getVehicleID());
        assertNull(deletedVehicle, "Deleted vehicle should not be found");
    }

    // Additional test to verify findByPlateNumber with non-existent plate
    @Test
    @Order(8)
    void findByPlateNumber_NotFound() {
        Optional<Vehicle> result = service.findByPlateNumber("NONEXISTENT");
        System.out.println("FIND BY PLATE (Non-existent) Result: " + result.orElse(null));
        assertFalse(result.isPresent(), "Should not find vehicle with non-existent plate number");
    }
}


//@SpringBootTest
//public class VehicleServiceTest {
//
//    @Autowired
//    private VehicleService service;
//
//    private Customer customer;
//    private Vehicle vehicle1;
//    private Vehicle vehicle2;
//
//    @BeforeEach
//    void setUp() {
//        // Example customer
//        customer = new Customer.Builder()
//                .setUserId(1L)
//                .setUserName("John")
//                .setUserSurname("Doe")
//                .setIsActive(true)
//                .setRoleDescription(User.RoleDescription.CLIENT)
//                .setCustomerDOB(LocalDate.of(1990, 1, 1))
//                .build();
//
//        // Example vehicles
//        vehicle1 = new Vehicle.Builder()
//                .setCarPlateNumber("ABC123")
//                .setCarMake("Toyota")
//                .setCarColour("Red")
//                .setCarModel("Corolla")
//                .setCustomer(customer)
//                .build();
//
//        vehicle2 = new Vehicle.Builder()
//                .setCarPlateNumber("XYZ789")
//                .setCarMake("Honda")
//                .setCarColour("Blue")
//                .setCarModel("Civic")
//                .setCustomer(customer)
//                .build();
//
//        // Persist vehicles for tests
//        service.create(vehicle1);
//        service.create(vehicle2);
//    }
//
//    @Test
//    void create() {
//        Vehicle newVehicle = new Vehicle.Builder()
//                .setCarPlateNumber("LMN456")
//                .setCarMake("Ford")
//                .setCarColour("Black")
//                .setCarModel("Focus")
//                .setCustomer(customer)
//                .build();
//
//        Vehicle created = service.create(newVehicle);
//        System.out.println("CREATE Result: " + created);
//        assertNotNull(created);
//    }
//
//    @Test
//    void read() {
//        Vehicle readVehicle = service.read(vehicle1.getVehicleID());
//        System.out.println("READ Result: " + readVehicle);
//        assertEquals(vehicle1.getCarPlateNumber(), readVehicle.getCarPlateNumber());
//    }
//
//    @Test
//    void update() {
//        Vehicle updatedVehicle = new Vehicle.Builder()
//                .copy(vehicle1)
//                .setCarPlateNumber("UPDATED123")
//                .build();
//
//        Vehicle result = service.update(updatedVehicle);
//        System.out.println("UPDATE Result: " + result);
//        assertEquals("UPDATED123", result.getCarPlateNumber());
//    }
//
//    @Test
//    void delete() {
//        boolean deleted = service.delete(vehicle2.getVehicleID());
//        System.out.println("DELETE Result: " + deleted);
//        assertTrue(deleted);
//    }
//
//    @Test
//    void findByPlateNumber() {
//        Optional<Vehicle> result = service.findByPlateNumber(vehicle1.getCarPlateNumber());
//        System.out.println("FIND BY PLATE Result: " + result.orElse(null));
//        assertTrue(result.isPresent());
//        assertEquals(vehicle1.getCarPlateNumber(), result.get().getCarPlateNumber());
//    }
//
//    @Test
//    void findByCustomerId() {
//        List<Vehicle> vehicles = service.findByCustomerId(customer.getUserId());
//        System.out.println("FIND BY CUSTOMER ID Result: " + vehicles);
//        assertTrue(vehicles.size() >= 2);
//    }
//
//    @Test
//    void findAll() {
//        List<Vehicle> allVehicles = service.findAll();
//        System.out.println("FIND ALL Result: " + allVehicles);
//        assertTrue(allVehicles.size() >= 2);
//    }
//}