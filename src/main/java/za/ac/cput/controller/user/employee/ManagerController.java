package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.service.user.employee.ManagerService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Manager")
public class ManagerController {

    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // CREATE (JSON - for account creation without image)
    @PostMapping("/create")
    public ResponseEntity<Manager> create(@RequestBody Manager manager) {
        try {
            Manager created = managerService.create(manager);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // CREATE WITH IMAGE (Multipart - for profile updates with image)
    @PostMapping("/create-with-image")
    public ResponseEntity<Manager> createWithImage(@RequestPart Manager manager,
                                                    @RequestPart(required = false) MultipartFile imageFile) {
        try {
            Manager created = managerService.create(manager, imageFile);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/image/{Id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long Id) {
        Manager manager = managerService.read(Id);
        byte[] imageFile = manager.getImageData();

        if (manager == null || manager.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(manager.getImageType()))
                .body(imageFile);
    }

    @GetMapping("/read/{Id}")
    public ResponseEntity<Manager> read(@PathVariable Long Id) {
        Manager manager = managerService.read(Id);
        if (manager == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(manager);
    }

    @PutMapping(value = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Manager> updateManager(
            @PathVariable Long id,
            @RequestPart("employee") Manager manager,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Manager updated = managerService.updateManager(id, manager, imageFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        if (managers == null || managers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(managers);
    }

    @DeleteMapping("/delete/{Id}")
    public void delete(@PathVariable Long Id) {
        managerService.delete(Id);
    }
}
