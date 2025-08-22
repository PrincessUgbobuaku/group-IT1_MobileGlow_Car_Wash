//Thaakirah Watson, 230037550
package za.ac.cput.controller.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.service.booking.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(service.create(vehicle));
    }

    // --- READ ---
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> read(@PathVariable String id) {
        return service.read(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable String id,
                                          @RequestBody Vehicle vehicle) {
        Vehicle updated = service.update(vehicle);
        return ResponseEntity.ok(updated);
    }

//    // --- UPDATE ---
//    @PutMapping("/{id}")
//    public ResponseEntity<Vehicle> update(@PathVariable String id, @RequestBody Vehicle vehicle) {
//        if (!id.equals(vehicle.getVehicleID())) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(service.update(vehicle));
//    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- GET ALL ---
    @GetMapping
    public List<Vehicle> getAll() {
        return service.getAll();
    }

    // --- EXTRA: find by customer ---
    @GetMapping("/by-customer/{customerId}")
    public List<Vehicle> findByCustomerID(@PathVariable String customerId) {
        return service.findByCustomerID(customerId);
    }

    // --- EXTRA: find by plate ---
    @GetMapping("/by-plate/{plate}")
    public ResponseEntity<Vehicle> findByPlate(@PathVariable String plate) {
        return service.findByPlateNumber(plate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}