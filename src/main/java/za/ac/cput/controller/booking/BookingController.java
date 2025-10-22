package za.ac.cput.controller.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.service.booking.BookingService;
import za.ac.cput.service.payment.PaymentService;
import za.ac.cput.service.user.employee.WashAttendantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final WashAttendantService washAttendantService;
    private final PaymentService paymentService;

    @Autowired
    public BookingController(BookingService bookingService,
                             WashAttendantService washAttendantService,
                             PaymentService paymentService) {
        this.bookingService = bookingService;
        this.washAttendantService = washAttendantService;
        this.paymentService = paymentService;
    }

    // -------------------------------
    // CRUD Endpoints
    // -------------------------------

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking created = bookingService.create(booking);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> read(@PathVariable Long id) {
        Booking found = bookingService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking) {
        if (!id.equals(booking.getBookingId())) {
            return ResponseEntity.badRequest().build();
        }
        Booking updated = bookingService.update(booking);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookingService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = bookingService.getAll();
        return ResponseEntity.ok(bookings);
    }

    // -------------------------------
    // Functional Endpoints
    // -------------------------------

    // Get all booked time slots for a specific date
    @GetMapping("/booked-timeslots")
    public ResponseEntity<List<LocalTime>> getBookedTimeSlots(@RequestParam String date) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);

            List<LocalTime> bookedTimes = bookingService.getAll().stream()
                    .filter(b -> b.getBookingDateTime().toLocalDate().equals(bookingDate))
                    .map(b -> b.getBookingDateTime().toLocalTime())
                    .toList();

            return ResponseEntity.ok(bookedTimes);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", date);
            return ResponseEntity.badRequest().build();
        }
    }

    // Get unavailable (fully booked) time slots
    @GetMapping("/unavailable-timeslots")
    public ResponseEntity<List<LocalTime>> getUnavailableSlots(@RequestParam String date) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);
            logger.info("Fetching unavailable slots for date: {}", bookingDate);

            int totalAttendants = washAttendantService.getAllWashAttendants().size();
            logger.info("Total wash attendants: {}", totalAttendants);

            List<Booking> bookings = bookingService.getAll().stream()
                    .filter(b -> b.getBookingDateTime().toLocalDate().equals(bookingDate))
                    .toList();

            Map<LocalTime, Long> bookingsPerSlot = bookings.stream()
                    .collect(Collectors.groupingBy(
                            b -> b.getBookingDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES),
                            Collectors.counting()));

            List<LocalTime> fullyBookedSlots = bookingsPerSlot.entrySet().stream()
                    .filter(e -> e.getValue() >= totalAttendants)
                    .map(Map.Entry::getKey)
                    .toList();

            return ResponseEntity.ok(fullyBookedSlots);

        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", date);
            return ResponseEntity.badRequest().build();
        }
    }

    // Check if a vehicle has a booking conflict
    @GetMapping("/check-conflict")
    public ResponseEntity<Boolean> checkVehicleConflict(
            @RequestParam("vehicleId") Long vehicleId,
            @RequestParam("bookingDateTime") String bookingDateTimeStr) {
        try {
            LocalDateTime bookingDateTime = LocalDateTime.parse(bookingDateTimeStr);
            boolean hasConflict = bookingService.hasBookingConflict(vehicleId, bookingDateTime);
            return ResponseEntity.ok(hasConflict);
        } catch (Exception e) {
            logger.error("Error checking conflict: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Get payment status for a booking
    @GetMapping("/{id}/payment-status")
    public ResponseEntity<Boolean> getPaymentStatus(@PathVariable Long id) {
        logger.info("Request for payment status of bookingId {}", id);
        boolean paid = paymentService.paymentExistsForBooking(id);
        logger.info("Payment status for bookingId {}: {}", id, paid);
        return ResponseEntity.ok(paid);
    }

    // Get bookings by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getBookingsByCustomer(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getAll().stream()
                .filter(b -> b.getVehicle() != null &&
                        b.getVehicle().getCustomer() != null &&
                        Objects.equals(b.getVehicle().getCustomer().getUserId(), customerId))
                .sorted(Comparator.comparing(Booking::getBookingDateTime).reversed())
                .toList();
        return ResponseEntity.ok(bookings);
    }

    // -------------------------------
    // Cancel Booking Endpoint (NEW)
    // -------------------------------
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            logger.info("Request received to cancel booking with ID: {}", id);

            Booking cancelled = bookingService.cancelBooking(id);
            logger.info("Booking with ID {} successfully cancelled.", id);

            return ResponseEntity.ok(cancelled);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to cancel booking with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error while cancelling booking: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }
}