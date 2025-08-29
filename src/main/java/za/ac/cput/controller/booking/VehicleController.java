//Thaakirah Watson, 230037550
package za.ac.cput.controller.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.service.booking.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(service.create(vehicle));
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> read(@PathVariable Long id) {
        Vehicle vehicle = service.read(id);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle = new Vehicle.Builder()
                .copy(vehicle)
                .setVehicleID(id)
                .build();
        Vehicle updated = service.update(vehicle);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Find by plate number
    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<Vehicle> findByPlateNumber(@PathVariable String plateNumber) {
        return service.findByPlateNumber(plateNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find vehicles by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Vehicle>> findByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.findByCustomerId(customerId));
    }
}