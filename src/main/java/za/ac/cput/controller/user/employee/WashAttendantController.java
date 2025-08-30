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

    @PostMapping
    public ResponseEntity<WashAttendant> create(@RequestBody WashAttendant washAttendant) {
        WashAttendant createdWashAttendant = washAttendantService.create(washAttendant);
        return ResponseEntity.ok(createdWashAttendant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WashAttendant> read(@PathVariable Long id) {
        WashAttendant washAttendant = washAttendantService.read(id);
        if (washAttendant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(washAttendant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WashAttendant> update(@PathVariable Long id, @RequestBody WashAttendant washAttendant) {
        if (!id.equals(washAttendant.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        WashAttendant updatedWashAttendant = washAttendantService.update(washAttendant);
        if (updatedWashAttendant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedWashAttendant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        WashAttendant washAttendant = washAttendantService.read(id);
        if (washAttendant == null) {
            return ResponseEntity.notFound().build();
        }

        washAttendantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WashAttendant>> getAll() {
        List<WashAttendant> washAttendants = washAttendantService.getAllWashAttendants();
        return ResponseEntity.ok(washAttendants);
    }
}
