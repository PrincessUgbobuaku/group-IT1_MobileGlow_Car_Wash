package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.service.user.employee.IWashAttendantService;

import java.util.List;

@RestController
@RequestMapping("/wash-attendants")
public class WashAttendantController {

    private final IWashAttendantService washAttendantService;

    @Autowired
    public WashAttendantController(IWashAttendantService washAttendantService) {
        this.washAttendantService = washAttendantService;
    }

    @PostMapping("/create")
    public ResponseEntity<WashAttendant> create(@RequestBody WashAttendant washAttendant) {
        WashAttendant createdWashAttendant = washAttendantService.create(washAttendant);
        return ResponseEntity.ok(createdWashAttendant);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<WashAttendant> read(@PathVariable Long id) {
        WashAttendant washAttendant = washAttendantService.read(id);
        if (washAttendant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(washAttendant);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WashAttendant> update(@PathVariable Long id, @RequestBody WashAttendant washAttendant) {
        washAttendant = new WashAttendant.Builder()
                .copy(washAttendant)
                .setUserId(id)
                .build();
        WashAttendant updated = washAttendantService.update(washAttendant);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        WashAttendant washAttendant = washAttendantService.read(id);
        if (washAttendant == null) {
            return ResponseEntity.notFound().build();
        }

        washAttendantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/random")
    public ResponseEntity<WashAttendant> getRandomAttendant() {
        try {
            WashAttendant randomAttendant = washAttendantService.getRandomWashAttendant();

            if (randomAttendant == null) {
                System.out.println("⚠️ No wash attendants available in DB.");
                return ResponseEntity.notFound().build();
            }

            System.out.println("✅ Returning wash attendant: " + randomAttendant);
            return ResponseEntity.ok(randomAttendant);
        } catch (Exception e) {
            System.err.println("❌ Error in getRandomAttendant: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }

    @GetMapping("/getAllWashAttendants")
    public ResponseEntity<List<WashAttendant>> getAll() {
        List<WashAttendant> washAttendants = washAttendantService.getAllWashAttendants();
        return ResponseEntity.ok(washAttendants);
    }


}
