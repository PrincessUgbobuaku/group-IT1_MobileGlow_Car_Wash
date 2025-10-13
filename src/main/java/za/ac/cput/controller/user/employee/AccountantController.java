package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.service.user.employee.IAccountantService;
import za.ac.cput.service.user.employee.AccountantService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Accountant")
public class AccountantController {

    private final AccountantService accountantService;

    @Autowired
    public AccountantController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("/create")
    public ResponseEntity<Accountant> create(@RequestBody Accountant accountant) {
        Accountant createdAccountant = accountantService.create(accountant);
        return ResponseEntity.ok(createdAccountant);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Accountant> read(@PathVariable Long id) {
        Accountant accountant = accountantService.read(id);
        if (accountant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountant);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Accountant> update(@PathVariable Long id, @RequestBody Accountant accountant) {
        accountant = new Accountant.Builder()
                .copy(accountant)
                .setUserId(id)
                .build();
        Accountant updated = accountantService.update(accountant);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Accountant accountant = accountantService.read(id);
        if (accountant == null) {
            return ResponseEntity.notFound().build();
        }

        accountantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllAccountants")
    public ResponseEntity<List<Accountant>> getAll() {
        List<Accountant> accountants = accountantService.getAllAccountants();
        return ResponseEntity.ok(accountants);
    }
}
