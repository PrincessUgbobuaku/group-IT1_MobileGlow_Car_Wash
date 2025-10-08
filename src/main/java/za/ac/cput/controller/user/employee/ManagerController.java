package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/create")
    public ResponseEntity<Manager> create(@RequestBody Manager manager) {
        Manager created = managerService.create(manager);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/read/{Id}")
    public ResponseEntity<Manager> read(@PathVariable Long Id) {
        Manager manager = managerService.read(Id);
        if (manager == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(manager);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Manager> update(@PathVariable Long id, @RequestBody Manager manager) {
        manager = new Manager.Builder()
                .copy(manager)
                .setUserId(id)
                .build();
        Manager updated = managerService.update(manager);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
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
