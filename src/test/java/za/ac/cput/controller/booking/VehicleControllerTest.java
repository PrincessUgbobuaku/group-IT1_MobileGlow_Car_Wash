// Thaakirah Watson, 230037550
package za.ac.cput.controller.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.User;
import za.ac.cput.service.booking.VehicleService;
import za.ac.cput.service.user.CustomerService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VehicleControllerTest {

    @Autowired
    private VehicleController controller;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CustomerService customerService;

    private Customer customer;
    private Vehicle vehicle1;
    private Vehicle vehicle2;

    // Generate unique plate numbers to avoid duplicates
    private String uniquePlate1;
    private String uniquePlate2;

    @BeforeEach
    void setUp() {
        // Generate unique plate numbers for each test run
        uniquePlate1 = "CTRL" + UUID.randomUUID().toString().substring(0, 4);
        uniquePlate2 = "CTRL" + UUID.randomUUID().toString().substring(0, 4);

        // Create and persist customer first
        customer = new Customer.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .setIsActive(true)
                .setRoleDescription(User.RoleDescription.CLIENT)
                .setCustomerDOB(LocalDate.of(1990, 1, 1))
                .build();

        // Save customer first using the service
        customer = customerService.create(customer);

        // Create vehicles with unique plate numbers
        vehicle1 = new Vehicle.Builder()
                .setCarPlateNumber(uniquePlate1)
                .setCarMake("Toyota")
                .setCarColour("Red")
                .setCarModel("Corolla")
                .setCustomer(customer)
                .build();

        vehicle2 = new Vehicle.Builder()
                .setCarPlateNumber(uniquePlate2)
                .setCarMake("Honda")
                .setCarColour("Blue")
                .setCarModel("Civic")
                .setCustomer(customer)
                .build();

        // Persist vehicles using controller and update references
        Map<String, Object> vehicle1Data = new HashMap<>();
        vehicle1Data.put("carPlateNumber", vehicle1.getCarPlateNumber());
        vehicle1Data.put("carMake", vehicle1.getCarMake());
        vehicle1Data.put("carColour", vehicle1.getCarColour());
        vehicle1Data.put("carModel", vehicle1.getCarModel());
        vehicle1Data.put("customerId", vehicle1.getCustomer().getUserId());
        
        Map<String, Object> vehicle2Data = new HashMap<>();
        vehicle2Data.put("carPlateNumber", vehicle2.getCarPlateNumber());
        vehicle2Data.put("carMake", vehicle2.getCarMake());
        vehicle2Data.put("carColour", vehicle2.getCarColour());
        vehicle2Data.put("carModel", vehicle2.getCarModel());
        vehicle2Data.put("customerId", vehicle2.getCustomer().getUserId());
        
        ResponseEntity<Vehicle> response1 = controller.create(vehicle1Data);
        ResponseEntity<Vehicle> response2 = controller.create(vehicle2Data);

        // Update references with the persisted entities (they now have IDs)
        vehicle1 = response1.getBody();
        vehicle2 = response2.getBody();

        System.out.println("Setup complete - Vehicle1 ID: " + vehicle1.getVehicleID() + ", Plate: " + uniquePlate1);
        System.out.println("Setup complete - Vehicle2 ID: " + vehicle2.getVehicleID() + ", Plate: " + uniquePlate2);
    }

    @Test
    @Order(1)
    void create() {
        String newPlate = "NEW" + UUID.randomUUID().toString().substring(0, 4);
        Vehicle newVehicle = new Vehicle.Builder()
                .setCarPlateNumber(newPlate)
                .setCarMake("Ford")
                .setCarColour("Black")
                .setCarModel("Focus")
                .setCustomer(customer)
                .build();

        Map<String, Object> newVehicleData = new HashMap<>();
        newVehicleData.put("carPlateNumber", newVehicle.getCarPlateNumber());
        newVehicleData.put("carMake", newVehicle.getCarMake());
        newVehicleData.put("carColour", newVehicle.getCarColour());
        newVehicleData.put("carModel", newVehicle.getCarModel());
        newVehicleData.put("customerId", newVehicle.getCustomer().getUserId());
        
        ResponseEntity<Vehicle> response = controller.create(newVehicleData);
        System.out.println("CREATE Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Your controller returns 200, not 201
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getVehicleID());
        assertEquals(newPlate, response.getBody().getCarPlateNumber());
    }

    @Test
    @Order(2)
    void read() {
        ResponseEntity<Vehicle> response = controller.read(vehicle1.getVehicleID());
        System.out.println("READ Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(vehicle1.getCarPlateNumber(), response.getBody().getCarPlateNumber());
        assertEquals(vehicle1.getVehicleID(), response.getBody().getVehicleID());
    }

    @Test
    @Order(3)
    void findByPlateNumber() {
        System.out.println("Searching for plate number: " + uniquePlate1);
        ResponseEntity<Vehicle> response = controller.findByPlateNumber(uniquePlate1);
        System.out.println("FIND BY PLATE Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(uniquePlate1, response.getBody().getCarPlateNumber());
    }

    @Test
    @Order(4)
    void update() {
        String updatedPlate = "UPD" + UUID.randomUUID().toString().substring(0, 4);

        // Build request map (this matches the controller's expected input)
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("carPlateNumber", updatedPlate);
        updateRequest.put("carMake", vehicle1.getCarMake());
        updateRequest.put("carModel", vehicle1.getCarModel());
        updateRequest.put("carColour", vehicle1.getCarColour());
        updateRequest.put("customerId", vehicle1.getCustomer().getUserId()); // sending customer reference

        // Call the update endpoint
        ResponseEntity<Vehicle> response = controller.update(vehicle1.getVehicleID(), updateRequest);

        System.out.println("UPDATE Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedPlate, response.getBody().getCarPlateNumber());
        assertEquals(vehicle1.getVehicleID(), response.getBody().getVehicleID());

        // Update reference for later tests
        vehicle1 = response.getBody();
        uniquePlate1 = updatedPlate;
    }


//    @Test
//    @Order(4)
//    void update() {
//        String updatedPlate = "UPD" + UUID.randomUUID().toString().substring(0, 4);
//        Vehicle updatedVehicle = new Vehicle.Builder()
//                .copy(vehicle1)
//                .setCarPlateNumber(updatedPlate)
//                .build();
//
//        // Call update (still returns Vehicle internally if your controller update endpoint does)
//        ResponseEntity<Map<String, Object>> response =
//                controller.update(vehicle1.getVehicleID(), updatedVehicle);
//
//        System.out.println("UPDATE Result: " + response.getBody());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//
//        // Extract values from the mapping object
//        Map<String, Object> responseBody = response.getBody();
//        assertEquals(updatedPlate, responseBody.get("carPlateNumber"));
//        assertEquals(vehicle1.getVehicleID(), responseBody.get("vehicleID"));
//
//        // Update reference for other tests (need to rebuild vehicle1 from map)
//        vehicle1 = new Vehicle.Builder()
//                .copy(vehicle1)
//                .setCarPlateNumber((String) responseBody.get("carPlateNumber"))
//                .build();
//
//        uniquePlate1 = updatedPlate;
//    }

    @Test
    @Order(5)
    void findByCustomerId() {
        ResponseEntity<List<Vehicle>> response = controller.findByCustomerId(customer.getUserId());
        System.out.println("FIND BY CUSTOMER ID Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2, "Should find at least 2 vehicles for this customer");

        // Verify that all returned vehicles belong to the correct customer
        response.getBody().forEach(vehicle -> {
            assertEquals(customer.getUserId(), vehicle.getCustomer().getUserId());
        });
    }

    @Test
    @Order(6)
    void findAll() {
        ResponseEntity<List<Vehicle>> response = controller.findAll();
        System.out.println("FIND ALL Result: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2, "Should find at least 2 vehicles in total");
    }

    @Test
    @Order(7)
    void delete() {
        Long vehicleIdToDelete = vehicle2.getVehicleID();
        ResponseEntity<Void> response = controller.delete(vehicleIdToDelete);
        System.out.println("DELETE Result: " + response.getStatusCode());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify deletion by trying to read the deleted vehicle
        ResponseEntity<Vehicle> readResponse = controller.read(vehicleIdToDelete);
        assertEquals(HttpStatus.NOT_FOUND, readResponse.getStatusCode());
    }

    @Test
    @Order(8)
    void findByPlateNumber_NotFound() {
        String nonExistentPlate = "NONE" + UUID.randomUUID().toString().substring(0, 4);
        ResponseEntity<Vehicle> response = controller.findByPlateNumber(nonExistentPlate);
        System.out.println("FIND BY PLATE (Non-existent) Result: " + response.getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(9)
    void read_NotFound() {
        ResponseEntity<Vehicle> response = controller.read(99999L);
        System.out.println("READ (Non-existent ID) Result: " + response.getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}