package za.ac.cput.controller.generic;

/* MobileGlow Car Wash
   Address Controller Class
   Author: Inga Zekani (221043756)
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.service.generic.AddressService;

import java.util.List;

@CrossOrigin(origins = " http://localhost:3002")
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/create")
    public ResponseEntity<Address> create(@RequestBody Address address) {
        Address saved = addressService.create(address);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/read/{addressID}")
    public ResponseEntity<?> read(@PathVariable Long addressID) {
        try {
            Address serviceReview = addressService.read(addressID);
            return ResponseEntity.ok(serviceReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Address> update(@PathVariable Long id, @RequestBody Address address) {
        if (!id.equals(address.getAddressID())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Address existing = addressService.read(id); // fetch existing
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            // Create a new Address object with updated fields using Builder
            Address updated = new Address.Builder()
                    .copy(existing) // copy existing data
                    .setStreetNumber(address.getStreetNumber()) // update new values
                    .setStreetName(address.getStreetName())
                    .setCity(address.getCity())
                    .setPostalCode(address.getPostalCode())
                    .build();

            // Save updated address
            Address saved = addressService.create(updated);
            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!addressService.delete(id)) {
            return ResponseEntity.notFound().build(); // Returns 404 if not found
        }
        return ResponseEntity.noContent().build(); // Returns 204 if successfully deleted
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Address>> getAll() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }
}