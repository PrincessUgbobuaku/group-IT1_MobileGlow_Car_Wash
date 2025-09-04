//Thaakirah Watson, 230037550
package za.ac.cput.controller.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.service.booking.VehicleService;
import za.ac.cput.service.user.CustomerService;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;
    private final CustomerService customerService;

    public VehicleController(VehicleService vehicleService, CustomerService customerService) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<Vehicle> create(@RequestBody Map<String, Object> request) {
        try {
            // Extract data from request
            String carPlateNumber = (String) request.get("carPlateNumber");
            String carMake = (String) request.get("carMake");
            String carColour = (String) request.get("carColour");
            String carModel = (String) request.get("carModel");

            // Handle customer ID - could be nested object or direct ID
            Long customerId;
            if (request.containsKey("customerId")) {
                Object customerIdObj = request.get("customerId");
                if (customerIdObj instanceof Number) {
                    customerId = ((Number) customerIdObj).longValue();
                } else {
                    customerId = Long.valueOf(customerIdObj.toString());
                }
            } else if (request.containsKey("customer")) {
                Map<String, Object> customerMap = (Map<String, Object>) request.get("customer");
                Object userIdObj = customerMap.get("userId");
                if (userIdObj instanceof Number) {
                    customerId = ((Number) userIdObj).longValue();
                } else {
                    customerId = Long.valueOf(userIdObj.toString());
                }
            } else {
                return ResponseEntity.badRequest().build();
            }

            // Fetch the customer by ID
            Customer customer = customerService.read(customerId);
            if (customer == null) {
                return ResponseEntity.badRequest().build();
            }

            // Create the vehicle with the fetched customer
            Vehicle vehicle = new Vehicle.Builder()
                    .setCarPlateNumber(carPlateNumber)
                    .setCarMake(carMake)
                    .setCarColour(carColour)
                    .setCarModel(carModel)
                    .setCustomer(customer)
                    .build();

            Vehicle createdVehicle = vehicleService.create(vehicle);
            return ResponseEntity.ok(createdVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // READ
    @GetMapping("/read/{id}")
    public ResponseEntity<Vehicle> read(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.read(id);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            // Extract data from request
            String carPlateNumber = (String) request.get("carPlateNumber");
            String carMake = (String) request.get("carMake");
            String carColour = (String) request.get("carColour");
            String carModel = (String) request.get("carModel");

            // Handle customer ID - could be nested object or direct ID
            Long customerId;
            if (request.containsKey("customerId")) {
                Object customerIdObj = request.get("customerId");
                if (customerIdObj instanceof Number) {
                    customerId = ((Number) customerIdObj).longValue();
                } else {
                    customerId = Long.valueOf(customerIdObj.toString());
                }
            } else if (request.containsKey("customer")) {
                Map<String, Object> customerMap = (Map<String, Object>) request.get("customer");
                Object userIdObj = customerMap.get("userId");
                if (userIdObj instanceof Number) {
                    customerId = ((Number) userIdObj).longValue();
                } else {
                    customerId = Long.valueOf(userIdObj.toString());
                }
            } else {
                return ResponseEntity.badRequest().build();
            }

            // Fetch the customer by ID
            Customer customer = customerService.read(customerId);
            if (customer == null) {
                return ResponseEntity.badRequest().build();
            }

            // Check if vehicle exists before updating
            Vehicle existingVehicle = vehicleService.read(id);
            if (existingVehicle == null) {
                return ResponseEntity.notFound().build();
            }

            // Create the updated vehicle with the fetched customer
            Vehicle updatedVehicle = new Vehicle.Builder()
                    .setVehicleID(id)
                    .setCarPlateNumber(carPlateNumber)
                    .setCarMake(carMake)
                    .setCarColour(carColour)
                    .setCarModel(carModel)
                    .setCustomer(customer)
                    .build();

            Vehicle updated = vehicleService.update(updatedVehicle);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return vehicleService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET ALL
    @GetMapping("/findAll")
    public ResponseEntity<List<Vehicle>> findAll() {
        try {
            List<Vehicle> vehicles = vehicleService.findAll();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Find by plate number
    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<Vehicle> findByPlateNumber(@PathVariable String plateNumber) {
        return vehicleService.findByPlateNumber(plateNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find vehicles by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Vehicle>> findByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.findByCustomerId(customerId));
    }

    // Simple vehicles endpoint without customer details to avoid serialization issues
    @GetMapping("/simple")
    public ResponseEntity<List<Map<String, Object>>> getSimpleVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.findAll();
            List<Map<String, Object>> simpleVehicles = new ArrayList<>();

            for (Vehicle vehicle : vehicles) {
                Map<String, Object> simpleVehicle = new HashMap<>();
                simpleVehicle.put("vehicleID", vehicle.getVehicleID());
                simpleVehicle.put("carPlateNumber", vehicle.getCarPlateNumber());
                simpleVehicle.put("carMake", vehicle.getCarMake());
                simpleVehicle.put("carModel", vehicle.getCarModel());
                simpleVehicle.put("carColour", vehicle.getCarColour());
                // Customer ID removed from simple vehicle data
                simpleVehicles.add(simpleVehicle);
            }

            return ResponseEntity.ok(simpleVehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Direct database query endpoint to bypass service layer
    @GetMapping("/direct-db")
    public ResponseEntity<String> directDatabaseQuery() {
        try {
            // This will help us see if the issue is in the service layer or repository
            List<Vehicle> vehicles = vehicleService.findAll();
            StringBuilder result = new StringBuilder();
            result.append("Direct database query result:\n");
            result.append("Total vehicles found: ").append(vehicles.size()).append("\n\n");

            if (vehicles.isEmpty()) {
                result.append("Database appears to be empty or connection issue.\n");
                result.append("Check:\n");
                result.append("1. Database connection (MySQL on localhost:3306)\n");
                result.append("2. Database name: mobileglowcarwashdb\n");
                result.append("3. Table: vehicle\n");
                result.append("4. Data exists in the table\n");
            } else {
                result.append("Vehicles in database:\n");
                for (Vehicle v : vehicles) {
                    result.append("- ID: ").append(v.getVehicleID())
                            .append(", Plate: ").append(v.getCarPlateNumber())
                            .append(", Make: ").append(v.getCarMake())
                            .append(", Model: ").append(v.getCarModel())
                            .append(", Colour: ").append(v.getCarColour())
                            .append("\n");
                }
            }

            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.ok("Database query error: " + e.getMessage() + "\n" + e.toString());
        }
    }
}