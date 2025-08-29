package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.service.user.employee.IAccountantService;

import java.util.List;

@RestController
@RequestMapping("/accountants")
public class AccountantController {

    private final IAccountantService accountantService;

    @Autowired
    public AccountantController(IAccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping
    public ResponseEntity<Accountant> create(@RequestBody Accountant accountant) {
        Accountant createdAccountant = accountantService.create(accountant);
        return ResponseEntity.ok(createdAccountant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accountant> read(@PathVariable Long id) {
        Accountant accountant = accountantService.read(id);
        if (accountant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Accountant> update(@PathVariable Long id, @RequestBody Accountant accountant) {
        if (!id.equals(accountant.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        Accountant updatedAccountant = accountantService.update(accountant);
        if (updatedAccountant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccountant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Accountant accountant = accountantService.read(id);
        if (accountant == null) {
            return ResponseEntity.notFound().build();
        }

        accountantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Accountant>> getAll() {
        List<Accountant> accountants = accountantService.getAllAccountants();
        return ResponseEntity.ok(accountants);
    }
}
