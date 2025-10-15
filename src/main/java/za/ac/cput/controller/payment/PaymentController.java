package za.ac.cput.controller.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.service.payment.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments") // Matches BookingController pattern
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // CREATE - POST /api/payments
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Payment payment) {
        logger.info("Received payment creation request: {}", payment);
        try {
            // Log the payment method before saving
            logger.info("Payment method before save: {}", payment.getPaymentMethod());

            // Optional: Validate payment method enum if you have an enum class
            // If not using enum, skip this block or add your own validation
            /*
            try {
                PaymentMethod.valueOf(payment.getPaymentMethod());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid payment method: {}", payment.getPaymentMethod());
                return ResponseEntity.badRequest().body("Invalid payment method");
            }
            */

            Payment created = paymentService.create(payment);
            logger.info("Payment created successfully with ID: {}", created.getPaymentId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // READ - GET /api/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Payment> read(@PathVariable Long id) {
        Payment found = paymentService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // UPDATE - PUT /api/payments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment payment) {
        if (!id.equals(payment.getPaymentId())) {
            logger.warn("Payment ID in path ({}) does not match ID in body ({})", id, payment.getPaymentId());
            return ResponseEntity.badRequest().body("Payment ID mismatch");
        }
        try {
            Payment updated = paymentService.update(payment);
            logger.info("Payment updated successfully with ID: {}", updated.getPaymentId());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating payment", e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // DELETE - DELETE /api/payments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = paymentService.delete(id);
        if (deleted) {
            logger.info("Payment deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Payment to delete not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // GET ALL - GET /api/payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    // GET payments by booking ID - GET /api/payments/booking/{bookingId}
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBookingID(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }
}