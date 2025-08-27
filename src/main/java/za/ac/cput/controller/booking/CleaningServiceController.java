package za.ac.cput.controller.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.service.booking.ICleaningServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/cleaningservice")
public class CleaningServiceController {

    private final ICleaningServiceService cleaningServiceService;

    @Autowired
    public CleaningServiceController(ICleaningServiceService cleaningServiceService) {
        this.cleaningServiceService = cleaningServiceService;
    }

    // ✅ CREATE - POST /api/cleaningservice
    @PostMapping
    public ResponseEntity<CleaningService> create(@RequestBody CleaningService cleaningService) {
        CleaningService created = cleaningServiceService.create(cleaningService);
        return ResponseEntity.ok(created);
    }

    // ✅ READ - GET /api/cleaningservice/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CleaningService> read(@PathVariable String id) {
        CleaningService found = cleaningServiceService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // ✅ UPDATE - PUT /api/cleaningservice/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CleaningService> update(@PathVariable String id, @RequestBody CleaningService cleaningService) {
        // Optional: Ensure path ID and body ID match (for integrity)
//        if (!id.equals(cleaningService.getCleaningServiceID())) {
//            return ResponseEntity.badRequest().build();
//        }
//        CleaningService updated = cleaningServiceService.update(cleaningService);
//        return ResponseEntity.ok(updated);

        // Force the path ID into the object — no need for it in the request body
        CleaningService updated = new CleaningService.Builder()
                .copy(cleaningService)
                .setCleaningServiceID(id)
                .build();

        return ResponseEntity.ok(cleaningServiceService.update(updated));
    }

    // ✅ DELETE - DELETE /api/cleaningservice/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = cleaningServiceService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ GET ALL - GET /api/cleaningservice
    @GetMapping
    public ResponseEntity<List<CleaningService>> getAll() {
        List<CleaningService> services = cleaningServiceService.getAll();
        return ResponseEntity.ok(services);
    }
}