//Thaakirah Watson, 230037550
package za.ac.cput.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.service.user.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        return ResponseEntity.ok(service.create(customer));
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Customer> read(@PathVariable Long id) {
        Customer customer = service.read(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer = new Customer.Builder()
                .copy(customer)
                .setUserId(id)
                .build();
        Customer updated = service.update(customer);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Find by surname
    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Customer>> findBySurname(@PathVariable String surname) {
        return ResponseEntity.ok(service.findBySurname(surname));
    }

    // Find active customers
    @GetMapping("/active")
    public ResponseEntity<List<Customer>> findActiveCustomers() {
        return ResponseEntity.ok(service.findActiveCustomers());
    }
}