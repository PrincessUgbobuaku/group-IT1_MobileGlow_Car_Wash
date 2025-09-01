package za.ac.cput.controller.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.service.booking.ICleaningServiceService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cleaningservice")
public class CleaningServiceController {

    private final ICleaningServiceService cleaningServiceService;

    @Autowired
    public CleaningServiceController(ICleaningServiceService cleaningServiceService) {
        this.cleaningServiceService = cleaningServiceService;
    }

    // ✅ CREATE - POST /api/cleaningservice
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CleaningService cleaningService) {
        try {
            System.out.println("Received CleaningService: " + cleaningService);
            System.out.println("Service Name: " + cleaningService.getServiceName());
            System.out.println("Category: " + cleaningService.getCategory());
            System.out.println("Price: " + cleaningService.getPriceOfService());
            System.out.println("Duration: " + cleaningService.getDuration());

            CleaningService created = cleaningServiceService.create(cleaningService);
            System.out.println("Created CleaningService: " + created);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", e.getMessage()));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid service data"));
        } catch (Exception e) {
            System.err.println("Error creating cleaning service: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Internal server error"));
        }
    }

    // ✅ READ - GET /api/cleaningservice/{id}
    @GetMapping("/read/{id}")
    public ResponseEntity<CleaningService> read(@PathVariable Long id) {
        CleaningService found = cleaningServiceService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // ✅ UPDATE - PUT /api/cleaningservice/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<CleaningService> update(@PathVariable Long id, @RequestBody CleaningService cleaningService) {
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = cleaningServiceService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ GET ALL - GET /api/cleaningservice
    @GetMapping("/getAll")
    public ResponseEntity<List<CleaningService>> getAll() {
        List<CleaningService> services = cleaningServiceService.getAll();
        return ResponseEntity.ok(services);
    }
}