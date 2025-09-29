package za.ac.cput.controller.booking;

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
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final WashAttendantService washAttendantService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    @Autowired
    private PaymentService paymentService;

    @Autowired
    public BookingController(BookingService bookingService, WashAttendantService washAttendantService) {
        this.bookingService = bookingService;
        this.washAttendantService = washAttendantService;
    }

    // CREATE - POST /api/bookings
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking created = bookingService.create(booking);
        return ResponseEntity.ok(created);
    }

    // READ - GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Booking> read(@PathVariable Long id) {
        Booking found = bookingService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // UPDATE - PUT /api/bookings/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking) {
        if (!id.equals(booking.getBookingId())) {
            return ResponseEntity.badRequest().build();
        }
        Booking updated = bookingService.update(booking);
        return ResponseEntity.ok(updated);
    }

    // DELETE - DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookingService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET ALL - GET /api/bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = bookingService.getAll();
        return ResponseEntity.ok(bookings);
    }

    // Existing: Return all time slots with at least one booking
    @GetMapping("/booked-timeslots")
    public ResponseEntity<List<LocalTime>> getBookedTimeSlots(@RequestParam String date) {
        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        List<Booking> bookings = bookingService.getAll().stream()
                .filter(b -> b.getBookingDateTime().toLocalDate().equals(bookingDate))
                .toList();

        List<LocalTime> bookedTimes = bookings.stream()
                .map(b -> b.getBookingDateTime().toLocalTime())
                .toList();

        return ResponseEntity.ok(bookedTimes);
    }

    // New: Return only fully booked time slots (based on attendant count)
    @GetMapping("/unavailable-timeslots")
    public ResponseEntity<List<LocalTime>> getUnavailableSlots(@RequestParam String date) {
        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
            logger.info("Fetching unavailable slots for date: {}", bookingDate);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", date);
            return ResponseEntity.badRequest().build();
        }

        int totalAttendants = washAttendantService.getAllWashAttendants().size();
        logger.info("Total wash attendants: {}", totalAttendants);

        List<Booking> bookings = bookingService.getAll().stream()
                .filter(b -> b.getBookingDateTime().toLocalDate().equals(bookingDate))
                .collect(Collectors.toList());

        logger.info("Bookings on {}: {}", bookingDate, bookings.size());
        bookings.forEach(b -> logger.info("Booking time: {}", b.getBookingDateTime().toLocalTime()));

        Map<LocalTime, Long> bookingsPerSlot = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getBookingDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES),
                        Collectors.counting()));

        bookingsPerSlot.forEach((time, count) -> logger.info("Time slot: {}, Bookings count: {}", time, count));

        List<LocalTime> fullyBookedSlots = bookingsPerSlot.entrySet().stream()
                .filter(e -> e.getValue() >= totalAttendants)
                .map(Map.Entry::getKey)
                .toList();

        logger.info("Fully booked slots: {}", fullyBookedSlots);

        return ResponseEntity.ok(fullyBookedSlots);
    }

    @GetMapping("/check-conflict")
    public ResponseEntity<Boolean> checkVehicleConflict(
            @RequestParam("vehicleId") Long vehicleId,
            @RequestParam("bookingDateTime") String bookingDateTimeStr) {
        try {
            LocalDateTime bookingDateTime = LocalDateTime.parse(bookingDateTimeStr);
            boolean hasConflict = bookingService.hasBookingConflict(vehicleId, bookingDateTime);
            return ResponseEntity.ok(hasConflict);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // New endpoint: GET /api/bookings/{id}/payment-status
    @GetMapping("/{id}/payment-status")
    public ResponseEntity<Boolean> getPaymentStatus(@PathVariable Long id) {
        logger.info("Request for payment status of bookingId {}", id);
        boolean paid = paymentService.paymentExistsForBooking(id);
        logger.info("Payment status for bookingId {}: {}", id, paid);
        return ResponseEntity.ok(paid);
    }

}