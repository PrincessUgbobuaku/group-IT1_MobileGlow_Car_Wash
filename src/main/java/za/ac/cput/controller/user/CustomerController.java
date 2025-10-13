//Thaakirah Watson, 230037550
package za.ac.cput.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.service.user.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<Customer> create(@RequestPart Customer customer,
                                           @RequestPart(required = false) MultipartFile imageFile) {
        try {
            Customer created = service.create(customer, imageFile);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // READ
    @GetMapping("/read/{id}")
    public ResponseEntity<Customer> read(@PathVariable Long id) {
        Customer customer = service.read(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    // UPDATE
    /*@PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id,
                                           @RequestPart Customer customer,
                                           @RequestPart(required = false) MultipartFile imageFile) {
        try {
            customer = new Customer.Builder()
                    .copy(customer)
                    .setUserId(id)
                    .build();
            Customer updated = imageFile != null && !imageFile.isEmpty() ?
                service.update(customer, imageFile) : service.update(customer);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }*/

    // UPDATE
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestPart("customer") Customer customer,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Customer updated = service.updateCustomer(id, customer, imageFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
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

    // Deactivate/Activate customer
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Customer> toggleCustomerStatus(@PathVariable Long id) {
        Customer customer = service.read(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        // Handle null isActive (default to true if null)
        Boolean currentStatus = customer.getIsActive();
        if (currentStatus == null) {
            currentStatus = true;
        }

        // Toggle the active status
        Customer updatedCustomer = new Customer.Builder()
                .copy(customer)
                .setIsActive(!currentStatus)
                .build();

        System.out.println("Toggling customer " + id + " from " + currentStatus + " to " + !currentStatus);

        Customer saved = service.update(updatedCustomer);
        System.out.println("Saved customer with isActive: " + saved.getIsActive());

        return ResponseEntity.ok(saved);
    }

    // Get customer image
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Customer customer = service.read(id);
        if (customer == null || customer.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(customer.getImageType()))
                .body(customer.getImageData());
    }
}
