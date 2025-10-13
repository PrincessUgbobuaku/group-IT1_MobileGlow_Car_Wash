package za.ac.cput.controller.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PutMapping(value = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Accountant> updateAccountant(
            @PathVariable Long id,
            @RequestPart("employee") Accountant accountant,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Accountant updated = accountantService.updateAccountant(id, accountant, imageFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
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

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Accountant accountant = accountantService.read(id);
        byte[] imageFile = accountant.getImageData();

        if (accountant == null || accountant.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(accountant.getImageType()))
                .body(imageFile);
    }
}
