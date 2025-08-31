package za.ac.cput.controller.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.service.booking.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ✅ CREATE - POST /api/bookings
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking created = bookingService.create(booking);
        return ResponseEntity.ok(created);
    }

    // ✅ READ - GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Booking> read(@PathVariable Long id) {
        Booking found = bookingService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // ✅ UPDATE - PUT /api/bookings/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking) {
        // Optional: Ensure path ID and body ID match (for integrity)
        if (!id.equals(booking.getBookingID())) {
            return ResponseEntity.badRequest().build();
        }
        Booking updated = bookingService.update(booking);
        return ResponseEntity.ok(updated);

        // Force the path ID into the object — no need for it in the request body

//        Booking updated = new Booking.Builder()
//                .copy(booking)
//                .setBookingID(id)
//                .build();
//
//        return ResponseEntity.ok(bookingService.update(updated));
    }

    // ✅ DELETE - DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookingService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ GET ALL - GET /api/bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = bookingService.getAll();
        return ResponseEntity.ok(bookings);
    }
}